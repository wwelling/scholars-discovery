package edu.tamu.scholars.discovery.auth.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import edu.tamu.scholars.discovery.auth.validator.PasswordConstraintValidator;

/**
 * Custom validation annotation that enforces password complexity requirements.
 * This annotation is applied at the type level and validates passwords at
 * runtime using the {@link PasswordConstraintValidator}. It ensures that
 * passwords meet the system's security requirements and complexity rules.
 * 
 * <p>
 * The validation is performed at the class level, allowing for cross-field
 * validation when necessary (e.g., comparing password and confirm password
 * fields).
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code @ValidPassword}(message = "Password does not meet complexity requirements")
 * public class UserCredentials {
 *     private String password;
 *     private String confirmPassword;
 * }
 * </pre>
 *
 * @see PasswordConstraintValidator
 * @see Constraint
 * @since 1.5.0
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface ValidPassword {

    /**
     * Defines the error message to be used when password validation fails. This
     * message can be a literal string or a property key for internationalization.
     *
     * @return the error message string
     */
    String message();

    /**
     * Defines the validation groups to which this constraint belongs. This allows
     * for validation to be applied only to certain groups of constraints. By
     * default, no groups are specified.
     *
     * @return an array of validation group classes
     */
    Class<?>[] groups() default {};

    /**
     * Defines the payload associated with the constraint. Can be used to carry
     * metadata information consumed by validation clients. By default, no payload
     * is specified.
     *
     * @return an array of payload classes
     */
    Class<? extends Payload>[] payload() default {};

}
