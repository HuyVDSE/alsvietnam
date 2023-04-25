package com.alsvietnam.annotations.validator.impl;

import com.alsvietnam.annotations.validator.ValuesAllowed;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

@ExtensionMethod(Extensions.class)
public class ValuesAllowedValidator implements ConstraintValidator<ValuesAllowed, String> {

    private List<String> expectedValues;

    private String returnMessage;

    @Override
    public void initialize(ValuesAllowed requiredValue) {
        expectedValues = Arrays.asList(requiredValue.values());
        returnMessage = requiredValue.message().concat(" " + expectedValues);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.isBlankOrNull()) {
            return true;
        }
        boolean valid = expectedValues.contains(value);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(returnMessage)
                    .addConstraintViolation();
        }
        return valid;
    }
}
