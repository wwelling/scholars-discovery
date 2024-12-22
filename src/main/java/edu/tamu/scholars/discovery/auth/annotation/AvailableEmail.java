package edu.tamu.scholars.discovery.auth.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import edu.tamu.scholars.discovery.auth.validator.EmailConstraintValidator;

/**
 * Custom validation annotation that verifies if an email address is available
 * for use. This annotation can be applied to fields and is validated at runtime
 * using the {@link EmailConstraintValidator}. It ensures that the email address
 * is not already in use within the system.
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * public class User {
 *     {@code @AvailableEmail}(message = "Email address is already in use")
 *     private String email;
 * }
 * </pre>
 *
 * @see EmailConstraintValidator
 * @see Constraint
 * @since 1.5.0
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = EmailConstraintValidator.class)
public @interface AvailableEmail {

    /**
     * Defines the error message to be used when the validation fails. This message
     * can be a literal string or a property key for internationalization.
     *
     * @return the error message string
     */
    String message();

    /**
     * Defines the validation groups to which this constraint belongs. This allows
     * for validation to be applied only to certain groups of constraints.
     *
     * @return an array of validation group classes
     */
    Class<?>[] groups() default {};

    /**
     * Defines the payload associated with the constraint. Can be used to carry
     * metadata information consumed by validation clients.
     *
     * @return an array of payload classes
     */
    Class<? extends Payload>[] payload() default {};

}
