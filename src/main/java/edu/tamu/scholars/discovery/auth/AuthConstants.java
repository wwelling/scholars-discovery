package edu.tamu.scholars.discovery.auth;

public class AuthConstants {

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
    public static final String METHOD_ARGUMENT_NOTVALID_EXCEPTION_MESSAGE = "RegistrationAdvice.methodArgumentNotValidExceptionMessage";
    public static final String LOGOUT_SUCCESS_MESSAGE = "CustomLogoutSuccessHandler.success";

    public static final String PASSWORD_INVALID_MESSAGE = "{Registration.passwordInvalid}";
    public static final String FIRST_NAME_SIZE_MESSAGE = "{Registration.firstNameSize}";
    public static final String LAST_NAME_SIZE_MESSAGE = "{Registration.lastNameSize}";
    public static final String EMAIL_REQUIRED_MESSAGE = "{Registration.emailRequired}";
    public static final String EMAIL_ALREADY_IN_USE_MESSAGE = "{Registration.emailAlreadyInUse}";
    public static final String EMAIL_INVALID_MESSAGE = "{Registration.emailInvalid}";

    // validations
    public static final int REGISTRATION_FIRST_NAME_MAX_LENGTH = 64;
    public static final int REGISTRATION_FIRST_NAME_MIN_LENGTH = 2;

    public static final int REGISTRATION_LAST_NAME_MAX_LENGTH = 64;
    public static final int REGISTRATION_LAST_NAME_MIN_LENGTH = 2;

    private AuthConstants() {

    }

}
