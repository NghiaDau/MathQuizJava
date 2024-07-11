package org.example.mathquiz.Config;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.CustomOAuth2User;
import org.example.mathquiz.Service.CustomOAuth2UserService;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SercurityConfig {
        private final CustomOAuth2UserService customOAuth2UserService;
        @Autowired
        private final UserService userService;

        @Bean
        public AuthenticationFailureHandler failureHandler() {
                return new FailureHandle(userService);
        }

        @Bean
        public AuthenticationSuccessHandler successHandler() {
                return new SuccessHandle(userService);
        }

        @Bean
        public AuthenticationSuccessHandler oauth2SuccessHandler() {
                return new SuccessHandleOauth(userService);
        }

        @Bean
        public UserDetailsService userDetailsService() {
                return new UserService();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                var auth = new DaoAuthenticationProvider();
                auth.setUserDetailsService(userDetailsService());
                auth.setPasswordEncoder(passwordEncoder());
                return auth;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http.csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                                        .requestMatchers("/css/**", "/js/**", "/", "/**", "/bootstrap/**",
                                                        "/icons/**", "/less/**", "/plugins/**","/oauth/**", "/images/**",
                                                        "/register", "/forgotpassword", "forgot_password",
                                                        "/error")
                                        .permitAll()
                                        .requestMatchers("/user","/user/**","/chapters/**","/grades/**","/quizs/**",
                                                        "/results","/results/**","/quizMatrices/**","/exams/**")
                                        .hasAnyAuthority("ADMIN")
                                        .requestMatchers("/chapters","/grades","/quizs","/quizMatrices","/exams")
                                        .hasAnyAuthority("USER","ADMIN")
                                        .anyRequest().authenticated())
                        .logout(logout -> logout.logoutUrl("/logout")
                                        .logoutSuccessUrl("/login")
                                        .deleteCookies("JSESSIONID")
                                        .invalidateHttpSession(true)
                                        .clearAuthentication(true)
                                        .permitAll())
                        .formLogin(formLogin -> formLogin.loginPage("/login")
                                        .usernameParameter("userName")
                                        .passwordParameter("passwordHash")
                                        .loginProcessingUrl("/login")
                                        .successHandler(successHandler())
                                        .failureHandler(failureHandler())
                                        .permitAll())
                        .oauth2Login(oauth2Login -> oauth2Login
                                        .loginPage("/login")
                                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                                        .successHandler(oauth2SuccessHandler())
                                        .permitAll())
                        .rememberMe(rememberMe -> rememberMe.key("hutech")
                                        .rememberMeCookieName("hutech")
                                        .tokenValiditySeconds(24 * 60 * 60)
                                        .userDetailsService(userDetailsService()))
                        .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/403"))
                        .sessionManagement(sessionManagement -> sessionManagement.maximumSessions(1)
                                                                                .expiredUrl("/login"))
                        .httpBasic(httpBasic -> httpBasic.realmName("hutech"))
                        .build();
        }

}
