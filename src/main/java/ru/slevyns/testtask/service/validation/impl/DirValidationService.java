package ru.slevyns.testtask.service.validation.impl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.slevyns.testtask.domain.DirRequest;
import ru.slevyns.testtask.domain.ValidationResult;
import ru.slevyns.testtask.service.validation.ValidationService;

import java.util.HashSet;
import java.util.Set;

import static ru.slevyns.testtask.util.DirValidationMeta.*;

@Service
public class DirValidationService implements ValidationService<DirRequest> {
    @Override
    public Set<ValidationResult> validate(DirRequest request) {
        Set<ValidationResult> result = new HashSet<>();
        validateDir(result, request.dirPath());
        validateMinWordLength(result, request.minLength());
        validateTopN(result, request.topN());

        return result;
    }

    private void validateDir(Set<ValidationResult> result, String dirPath) {
        if (Strings.isBlank(dirPath)) {
            result.add(new ValidationResult(DIR_PATH_ERROR, DIR_PATH_PARAM));
        }
    }

    private void validateMinWordLength(Set<ValidationResult> result, int minWordLength){
        if (minWordLength <= 0){
            result.add(new ValidationResult(MIN_WORD_LENGTH_ERROR, MIN_WORD_PARAM));
        }
    }

    private void validateTopN(Set<ValidationResult> result, int topN){
        if (topN <= 0){
            result.add(new ValidationResult(TOP_N_ERROR, TOP_N_PARAM));
        }
    }
}
