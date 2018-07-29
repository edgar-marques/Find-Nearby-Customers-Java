package com.example.script.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {RegularFileValidator.class})
public @interface RegularFile {
    /**
     *
     * @return
     */
    String message() default "file either does not exist or is not a regular file: ${validatedValue}";

    /**
     *
     * @return
     */
    Class<?>[] groups() default { };

    /**
     *
     * @return
     */
    Class<? extends Payload>[] payload() default { };
}
