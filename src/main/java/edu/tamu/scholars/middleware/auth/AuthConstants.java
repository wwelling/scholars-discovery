package edu.tamu.scholars.middleware.auth;

import edu.tamu.scholars.middleware.MiddlewareApplication;
import edu.tamu.scholars.middleware.auth.details.CustomUserDetails;

/**
 * Application initialized constants for use at runtime statically.
 * These are set in {@link MiddlewareApplication} and used in {@link CustomUserDetails}.
 */
public class AuthConstants {

    public static int PASSWORD_DURATION_IN_DAYS;

    public static int PASSWORD_MIN_LENGTH;

    public static int PASSWORD_MAX_LENGTH;

}
