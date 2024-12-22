package edu.tamu.scholars.discovery.auth.handler;

import static edu.tamu.scholars.discovery.AppConstants.EMPTY_OBJECT_ARRAY;
import static edu.tamu.scholars.discovery.auth.AuthConstants.LOGOUT_SUCCESS_MESSAGE;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private MessageSource messageSource;

    public CustomLogoutSuccessHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void onLogoutSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        String message = messageSource.getMessage(
            LOGOUT_SUCCESS_MESSAGE,
            EMPTY_OBJECT_ARRAY,
            LocaleContextHolder.getLocale());
        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        response.getWriter()
            .write(message);
        response.getWriter()
            .flush();
        response.getWriter()
            .close();
    }

}
