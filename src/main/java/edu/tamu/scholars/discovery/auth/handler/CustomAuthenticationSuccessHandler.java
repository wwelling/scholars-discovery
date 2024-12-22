package edu.tamu.scholars.discovery.auth.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper;

    public CustomAuthenticationSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream()
            .write(objectMapper.writeValueAsBytes(authentication.getPrincipal()));
    }

}
