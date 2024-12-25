package edu.tamu.scholars.discovery.auth.controller.request;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_EMAIL_ALREADY_IN_USE_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_EMAIL_INVALID_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_EMAIL_REQUIRED_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_FIRST_NAME_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_FIRST_NAME_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_FIRST_NAME_SIZE_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_LAST_NAME_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_LAST_NAME_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_LAST_NAME_SIZE_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.REGISTRATION_PASSWORD_INVALID_MESSAGE;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.auth.annotation.AvailableEmail;
import edu.tamu.scholars.discovery.auth.annotation.ValidPassword;
import edu.tamu.scholars.discovery.auth.validator.group.CompleteRegistration;
import edu.tamu.scholars.discovery.auth.validator.group.SubmitRegistration;

@Getter
@Setter
@NoArgsConstructor
@ValidPassword(
    groups = CompleteRegistration.class,
    message = REGISTRATION_PASSWORD_INVALID_MESSAGE)
public class Registration {

    @Size(
        max = REGISTRATION_FIRST_NAME_MAX_LENGTH,
        min = REGISTRATION_FIRST_NAME_MIN_LENGTH,
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = REGISTRATION_FIRST_NAME_SIZE_MESSAGE)
    private String firstName;

    @Size(
        max = REGISTRATION_LAST_NAME_MAX_LENGTH,
        min = REGISTRATION_LAST_NAME_MIN_LENGTH,
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = REGISTRATION_LAST_NAME_SIZE_MESSAGE)
    private String lastName;

    @NotNull(
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = REGISTRATION_EMAIL_REQUIRED_MESSAGE)
    @NotEmpty(
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = REGISTRATION_EMAIL_REQUIRED_MESSAGE)
    @AvailableEmail(
        groups = SubmitRegistration.class,
        message = REGISTRATION_EMAIL_ALREADY_IN_USE_MESSAGE)
    @Email(
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = REGISTRATION_EMAIL_INVALID_MESSAGE)
    private String email;

    @JsonInclude(NON_NULL)
    private String password;

    @JsonInclude(NON_NULL)
    private String confirm;

}
