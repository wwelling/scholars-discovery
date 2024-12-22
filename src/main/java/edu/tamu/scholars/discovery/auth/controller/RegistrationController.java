package edu.tamu.scholars.discovery.auth.controller;

import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_REST_BASE_PATH;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.auth.controller.exception.RegistrationException;
import edu.tamu.scholars.discovery.auth.controller.request.Registration;
import edu.tamu.scholars.discovery.auth.model.User;
import edu.tamu.scholars.discovery.auth.service.RegistrationService;
import edu.tamu.scholars.discovery.auth.validator.group.CompleteRegistration;
import edu.tamu.scholars.discovery.auth.validator.group.SubmitRegistration;

@Validated
@RestController
@RequestMapping(REGISTRATION_REST_BASE_PATH)
public class RegistrationController {

    private RegistrationService registrationService;

    RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Registration> submit(
        @RequestBody
        @Validated(SubmitRegistration.class)
        Registration registration
    ) throws JsonProcessingException {
        return ResponseEntity.ok(registrationService.submit(registration));
    }

    @GetMapping
    public ResponseEntity<Registration> confirm(
        @RequestParam(required = true)
        String key
    ) throws IOException, RegistrationException {
        return ResponseEntity.ok(registrationService.confirm(key));
    }

    @PutMapping
    public ResponseEntity<User> complete(
        @RequestParam(required = true)
        String key,
        @RequestBody
        @Validated(CompleteRegistration.class)
        Registration registration
    ) throws RegistrationException {
        return ResponseEntity.ok(registrationService.complete(key, registration));
    }

}
