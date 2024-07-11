package org.example.mathquiz.Config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mathquiz.Controller.ReCaptchaResponse;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
@Component
public class SuccessHandle extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Value("${recaptcha.url}")
    private String recaptchaServerURL;


    @Autowired
    private final UserService userService;

    public SuccessHandle(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String gRecaptchaReponse = request.getParameter("g-recaptcha-response");
        if(!verifyReCAPCHA(gRecaptchaReponse)) {
            String errorMessage1 = "Please Verify captcha response";
            request.getSession().setAttribute("errorMessage", errorMessage1);
            setDefaultTargetUrl("/login?error=true");
            super.onAuthenticationSuccess(request, response, authentication);
        }
        User user = ((User)authentication.getPrincipal());
        request.getSession().setAttribute("userLogin", user);
        if(!user.isStatus()){
            userService.resetLockAccount(user);
        }else{
            String errorMessage;
            if (user.getLockExpired() == null) {
                errorMessage = "Vui lòng liên hệ Admin";
                request.getSession().setAttribute("errorMessage", errorMessage);
                setDefaultTargetUrl("/login?error=true");
                super.onAuthenticationSuccess(request, response, authentication);
            } else {
                if(user.getLockExpired().getTime() > System.currentTimeMillis()){
                    errorMessage = "Tài Khoản Bị Khóa";
                    request.getSession().setAttribute("errorMessage", errorMessage);
                    setDefaultTargetUrl("/login?error=true");
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            }
            userService.resetLockAccount(user);
        }

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
        if(isAdmin){
            setDefaultTargetUrl("/user");
        }else{
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
    private boolean verifyReCAPCHA(String RecaptchaResponse) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", RecaptchaResponse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ReCaptchaResponse response = restTemplate.postForObject(recaptchaServerURL, request , ReCaptchaResponse.class);

        System.out.println("Successfully recaptcha response: " + response.isSuccess());
        System.out.println("HostName: " + response.getHostname());
        System.out.println("Challenge: " + response.getChallenge_ts());
        if(response.getErrorCode() != null) {
            for (String error : response.getErrorCode()) {
                System.out.println("\t" + error);
            }
        }
        return response.isSuccess();
    }
}
