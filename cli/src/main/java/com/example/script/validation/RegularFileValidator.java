package com.example.script.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

/**
 *
 */
class RegularFileValidator implements ConstraintValidator<RegularFile, File> {

    /**
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(RegularFile constraintAnnotation) {

    }

    /**
     *
     * @param file
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(File file, ConstraintValidatorContext constraintValidatorContext) {
        return file.isFile();
    }
}
