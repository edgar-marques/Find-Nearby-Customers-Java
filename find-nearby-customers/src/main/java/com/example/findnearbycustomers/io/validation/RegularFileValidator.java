package com.example.findnearbycustomers.io.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

public class RegularFileValidator implements ConstraintValidator<RegularFile, File> {
    @Override
    public void initialize(RegularFile constraintAnnotation) {

    }

    @Override
    public boolean isValid(File file, ConstraintValidatorContext constraintValidatorContext) {
        return file.isFile();
    }
}
