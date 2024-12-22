package edu.tamu.scholars.middleware.auth.controller.request;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static edu.tamu.scholars.middleware.auth.AuthConstants.EMAIL_ALREADY_IN_USE_MESSAGE;
import static edu.tamu.scholars.middleware.auth.AuthConstants.EMAIL_INVALID_MESSAGE;
import static edu.tamu.scholars.middleware.auth.AuthConstants.EMAIL_REQUIRED_MESSAGE;
import static edu.tamu.scholars.middleware.auth.AuthConstants.FIRST_NAME_SIZE_MESSAGE;
import static edu.tamu.scholars.middleware.auth.AuthConstants.LAST_NAME_SIZE_MESSAGE;
import static edu.tamu.scholars.middleware.auth.AuthConstants.PASSWORD_INVALID_MESSAGE;
import static edu.tamu.scholars.middleware.auth.AuthConstants.REGISTRATION_FIRST_NAME_MAX_LENGTH;
import static edu.tamu.scholars.middleware.auth.AuthConstants.REGISTRATION_FIRST_NAME_MIN_LENGTH;
import static edu.tamu.scholars.middleware.auth.AuthConstants.REGISTRATION_LAST_NAME_MAX_LENGTH;
import static edu.tamu.scholars.middleware.auth.AuthConstants.REGISTRATION_LAST_NAME_MIN_LENGTH;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import edu.tamu.scholars.middleware.auth.annotation.AvailableEmail;
import edu.tamu.scholars.middleware.auth.annotation.ValidPassword;
import edu.tamu.scholars.middleware.auth.validator.group.CompleteRegistration;
import edu.tamu.scholars.middleware.auth.validator.group.SubmitRegistration;

@ValidPassword(
    groups = CompleteRegistration.class,
    message = PASSWORD_INVALID_MESSAGE)
public class Registration {

    @Size(
        max = REGISTRATION_FIRST_NAME_MAX_LENGTH,
        min = REGISTRATION_FIRST_NAME_MIN_LENGTH,
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = FIRST_NAME_SIZE_MESSAGE)
    private String firstName;

    @Size(
        max = REGISTRATION_LAST_NAME_MAX_LENGTH,
        min = REGISTRATION_LAST_NAME_MIN_LENGTH,
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = LAST_NAME_SIZE_MESSAGE)
    private String lastName;

    @NotNull(
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = EMAIL_REQUIRED_MESSAGE)
    @NotEmpty(
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = EMAIL_REQUIRED_MESSAGE)
    @AvailableEmail(
        groups = SubmitRegistration.class,
        message = EMAIL_ALREADY_IN_USE_MESSAGE)
    @Email(
        groups = {
            SubmitRegistration.class,
            CompleteRegistration.class
        },
        message = EMAIL_INVALID_MESSAGE)
    private String email;

    @JsonInclude(NON_NULL)
    private String password;

    @JsonInclude(NON_NULL)
    private String confirm;

    public Registration() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

}
