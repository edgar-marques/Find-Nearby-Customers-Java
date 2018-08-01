package com.example.script.validation;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.io.File;
import java.lang.annotation.Annotation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class RegularFileValidatorTest {

    private RegularFileValidator validator = new RegularFileValidator();

    @Test
    public void initialize_shouldBeANoOp() {
        // given

        // instantiate the annotation type
        RegularFile regularFile = new RegularFile() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }
        };

        // when
        validator.initialize(regularFile);

        // then
        // there should no side-effects nor exceptions thrown
    }

    @Test
    public void isValid_shouldReturnFalse_whenFileDoesNotExist() {
        // given
        File nonExistingFile = new File("does not exist");

        // when
        boolean valid = validator.isValid(nonExistingFile, mock(ConstraintValidatorContext.class));

        // then
        assertThat(valid, is(false));
    }

    @Test
    public void isValid_shouldReturnFalse_whenFileIsNotARegularFile() {
        // given
        File directory = FileUtils.getTempDirectory();

        // when
        boolean valid = validator.isValid(directory, mock(ConstraintValidatorContext.class));

        // then
        assertThat(valid, is(false));
    }

    @Test
    public void isValid_shouldReturnTrue_whenFileIsARegularFile() throws Exception {
        // given
        File file = File.createTempFile("test", null);

        // when
        boolean valid = validator.isValid(file, mock(ConstraintValidatorContext.class));

        // then
        assertThat(valid, is(true));

        // clean up
        file.delete();
    }
}
