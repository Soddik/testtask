package ru.slevyns.testtask.service.validation.impl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.slevyns.testtask.dto.db.DbRequest;
import ru.slevyns.testtask.dto.ValidationResult;
import ru.slevyns.testtask.service.validation.ValidationService;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static ru.slevyns.testtask.util.DbValidationMeta.DATE_TIME_PARAM;
import static ru.slevyns.testtask.util.DbValidationMeta.EMPTY_ERROR;
import static ru.slevyns.testtask.util.DbValidationMeta.REGEX_NOT_MATCH_ERROR;
import static ru.slevyns.testtask.util.DbValidationMeta.TABLE_NAME_PARAM;

@Service
public class DbValidationService implements ValidationService<DbRequest> {
    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])");

    @Override
    public Set<ValidationResult> validate(DbRequest dbRequest) {
        var result = new HashSet<ValidationResult>();
        validateTableName(result, dbRequest.tableName());
        validateDateTime(result, dbRequest.dateTime());

        return result;
    }

    private void validateTableName(Set<ValidationResult> result, String tableName) {
        validateNull(result, tableName, TABLE_NAME_PARAM);
        validateRegex(result, tableName, TABLE_NAME_PARAM, TABLE_NAME_PATTERN);
    }

    private void validateDateTime(Set<ValidationResult> result, String dateTime) {
        validateNull(result, dateTime, DATE_TIME_PARAM);
        validateRegex(result, dateTime, DATE_TIME_PARAM, DATE_TIME_PATTERN);
    }

    private void validateNull(Set<ValidationResult> result, String paramValue, String paramName) {
        if (Strings.isBlank(paramValue)) {
            result.add(new ValidationResult(EMPTY_ERROR.formatted(paramName), paramName));
        }
    }

    private void validateRegex(Set<ValidationResult> result, String paramValue, String paramName, Pattern pattern) {
        if (!Strings.isBlank(paramValue)) {
            var isValid = pattern.matcher(paramValue).matches();
            if (!isValid) {
                result.add(new ValidationResult(String.format(REGEX_NOT_MATCH_ERROR, paramName, pattern), paramValue));
            }
        }
    }
}
