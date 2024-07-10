package org.example.mathquiz.Config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@Component
public class SuccessHandle extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private final UserService userService;

    public SuccessHandle(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = ((User)authentication.getPrincipal());
        request.getSession().setAttribute("userLogin", user);
        if(!user.isEnabled()){
            userService.resetLockAccount(user);
        }else{
            if(user.getLockExpired().getTime() > System.currentTimeMillis()){
                String errorMessage = "Tài Khoản Bị Khóa";
                request.getSession().setAttribute("errorMessage", errorMessage);
                setDefaultTargetUrl("/login?error=true");
                super.onAuthenticationSuccess(request, response, authentication);
            }else{
                userService.resetLockAccount(user);
            }
        }
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
        if(isAdmin){
            setDefaultTargetUrl("/user");
        }else{
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
