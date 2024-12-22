package edu.tamu.scholars.middleware.auth.controller;

import static edu.tamu.scholars.middleware.auth.AuthConstants.USERS_REST_BASE_PATH;
import static edu.tamu.scholars.middleware.auth.AuthConstants.USERS_REST_SELF_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.middleware.auth.model.User;

@RestController
@RequestMapping(USERS_REST_BASE_PATH)
public class UserController {

    @GetMapping(
        path = USERS_REST_SELF_PATH,
        produces = APPLICATION_JSON_VALUE)
    public User user(@AuthenticationPrincipal User user) {
        return user;
    }

}
