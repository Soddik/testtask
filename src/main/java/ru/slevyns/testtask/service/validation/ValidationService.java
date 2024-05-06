package ru.slevyns.testtask.service.validation;

import ru.slevyns.testtask.dto.ValidationResult;

import java.util.Set;
/**
 * Validation service for requests
 * */
public interface ValidationService<R> {
    Set<ValidationResult> validate(R r);
}
