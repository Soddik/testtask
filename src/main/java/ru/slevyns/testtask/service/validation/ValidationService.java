package ru.slevyns.testtask.service.validation;

import ru.slevyns.testtask.dto.dir.ValidationResult;

import java.util.Set;

public interface ValidationService<R> {
    Set<ValidationResult> validate(R r);
}
