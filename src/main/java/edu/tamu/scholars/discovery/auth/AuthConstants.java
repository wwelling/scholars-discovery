package edu.tamu.scholars.discovery.auth;

public class AuthConstants {

    // persistent names
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_ID_COLUMN_NAME = "id";
    public static final String USERS_FIRST_NAME_COLUMN_NAME = "first_name";
    public static final String USERS_LAST_NAME_COLUMN_NAME = "last_name";
    public static final String USERS_EMAIL_COLUMN_NAME = "email";
    public static final String USERS_PASSWORD_COLUMN_NAME = "password";
    public static final String USERS_ROLE_COLUMN_NAME = "role";
    public static final String USERS_CREATED_COLUMN_NAME = "created";
    public static final String USERS_TIMESTAMP_COLUMN_NAME = "timestamp";
    public static final String USERS_CONFIRMED_COLUMN_NAME = "confirmed";
    public static final String USERS_ACTIVE_COLUMN_NAME = "active";
    public static final String USERS_ENABLED_COLUMN_NAME = "enabled";

    public static final String USERS_OLD_PASSWORDS_TABLE_NAME = "user_old_passwords";
    public static final String USERS_OLD_PASSWORDS_JOIN_COLUMN_NAME = "user_id";

    // request mapping paths
    public static final String USERS_REST_BASE_PATH = "${discovery.users.base-path:/users}";
    public static final String USERS_REST_SELF_PATH = "${discovery.users.base-path:/self}";

    public static final String REGISTRATION_REST_BASE_PATH = "${discovery.registration.base-path:/registration}";

    // message channels
    public static final String USERS_CHANNEL = "${discovery.users.message-channel:/queue/users}";

    // configuration property prefix
    public static final String MIDDLEWARE_AUTH = "discovery.auth";
    public static final String MIDDLEWARE_AUTH_PASSWORD = "discovery.auth.password";
    public static final String MIDDLEWARE_AUTH_TOKEN = "discovery.auth.token";

    // defaults
    public static final int DEFAULT_REGISTRATION_TOKEN_DURATION = 14;

    public static final int DEFAULT_PASSWORD_DURATION = 180;
    public static final int DEFAULT_PASSWORD_MAX_LENGTH = 64;
    public static final int DEFAULT_PASSWORD_MIN_LENGTH = 8;

    public static final int DEFAULT_TOKEN_SERVER_INTEGER = 1;
    public static final String DEFAULT_TOKEN_SERVER_SECRET = "wKFkxTX54UzKx6xCYnC8WlEI2wtOy0PR";
    public static final int DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES = 64;

    // messages
    public static final String LOGOUT_SUCCESS_MESSAGE = "CustomLogoutSuccessHandler.success";

    public static final String EMAIL_NOT_FOUND_MESSAGE = "CustomUserDetailsService.emailNotFound";

    public static final String METHOD_ARGUMENT_NOTVALID_EXCEPTION_MESSAGE = "RegistrationAdvice.methodArgumentNotValidExceptionMessage";

    public static final String CONFIRMATION_EMAIL_SUBJECT_MESSAGE = "RegistrationService.confirmationEmailSubject";
    public static final String EMAIL_ALREADY_CONFIRMED_MESSAGE = "RegistrationService.emailAlreadyConfirmed";
    public static final String TOKEN_EXPIRED_MESSAGE = "RegistrationService.tokenExpired";
    public static final String UNABLE_TO_CONFIRM_EMAIL_NOT_FOUND_MESSAGE = "RegistrationService.unableToConfirmEmailNotFound";
    public static final String EMAIL_NOT_CONFIRMED_MESSAGE = "RegistrationService.emailNotConfirmed";
    public static final String UNABLE_TO_COMPLETE_EMAIL_NOT_FOUND_MESSAGE = "RegistrationService.unableToCompleteEmailNotFound";

    public static final String PASSWORDS_DO_NOT_MATCH_MESSAGE = "PasswordConstraintValidator.passwordsDoNotMatch";

    public static final String REGISTRATION_PASSWORD_INVALID_MESSAGE = "{Registration.passwordInvalid}";
    public static final String REGISTRATION_FIRST_NAME_SIZE_MESSAGE = "{Registration.firstNameSize}";
    public static final String REGISTRATION_LAST_NAME_SIZE_MESSAGE = "{Registration.lastNameSize}";
    public static final String REGISTRATION_EMAIL_REQUIRED_MESSAGE = "{Registration.emailRequired}";
    public static final String REGISTRATION_EMAIL_ALREADY_IN_USE_MESSAGE = "{Registration.emailAlreadyInUse}";
    public static final String REGISTRATION_EMAIL_INVALID_MESSAGE = "{Registration.emailInvalid}";

    public static final String USER_FIRST_NAME_REQUIRED_MESSAGE = "{User.firstNameRequired}";
    public static final String USER_FIRST_NAME_SIZE_MESSAGE = "{User.firstNameSize}";
    public static final String USER_LAST_NAME_REQUIRED_MESSAGE = "{User.lastNameRequired}";
    public static final String USER_LAST_NAME_SIZE_MESSAGE = "{User.lastNameSize}";
    public static final String USER_EMAIL_REQUIRED_MESSAGE = "{User.emailRequired}";
    public static final String USER_EMAIL_INVALID_MESSAGE = "{User.emailInvalid}";
    public static final String USER_ROLE_REQUIRED_MESSAGE = "{User.roleRequired}";

    // validations
    public static final int REGISTRATION_FIRST_NAME_MAX_LENGTH = 64;
    public static final int REGISTRATION_FIRST_NAME_MIN_LENGTH = 2;
    public static final int REGISTRATION_LAST_NAME_MAX_LENGTH = 64;
    public static final int REGISTRATION_LAST_NAME_MIN_LENGTH = 2;

    public static final int USER_FIRST_NAME_MAX_LENGTH = 64;
    public static final int USER_FIRST_NAME_MIN_LENGTH = 2;
    public static final int USER_LAST_NAME_MAX_LENGTH = 64;
    public static final int USER_LAST_NAME_MIN_LENGTH = 2;

    private AuthConstants() {

    }

}
