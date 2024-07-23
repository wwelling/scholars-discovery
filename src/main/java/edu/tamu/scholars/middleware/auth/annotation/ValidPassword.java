package edu.tamu.scholars.middleware.auth.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import edu.tamu.scholars.middleware.auth.model.User;
import edu.tamu.scholars.middleware.auth.validator.PasswordConstraintValidator;

/**
 * Annotation to declare validated {@link User} model password is available.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface ValidPassword {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
