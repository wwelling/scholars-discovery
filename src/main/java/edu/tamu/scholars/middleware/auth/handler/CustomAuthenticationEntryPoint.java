package edu.tamu.scholars.middleware.auth.handler;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Spring Boot autoconfigured custom {@link AuthenticationEntryPoint}. Customized to
 * return 401 status and write exception message to response.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException, ServletException {
        response.setStatus(SC_UNAUTHORIZED);
        response.getWriter().write(exception.getMessage());
    }

}
