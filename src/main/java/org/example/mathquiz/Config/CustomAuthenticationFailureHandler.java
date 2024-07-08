package org.example.mathquiz.Config;

import jakarta.servlet.ServletException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "Sai tên tài khoản hoặc mật khẩu";
        System.out.println("AAA");
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login?error=true");
    }
}
