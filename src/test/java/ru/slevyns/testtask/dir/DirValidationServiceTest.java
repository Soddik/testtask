package ru.slevyns.testtask.dir;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.slevyns.testtask.dto.dir.DirRequest;
import ru.slevyns.testtask.dto.ValidationResult;
import ru.slevyns.testtask.service.validation.ValidationService;
import ru.slevyns.testtask.service.validation.impl.DirValidationService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.slevyns.testtask.dir.DirTestMeta.CONTROLLER_TEST_DIR_PATH;
import static ru.slevyns.testtask.dir.DirTestMeta.MIN_LENGTH;
import static ru.slevyns.testtask.dir.DirTestMeta.TOP_NUM;
import static ru.slevyns.testtask.util.DirValidationMeta.DIR_PATH_ERROR;
import static ru.slevyns.testtask.util.DirValidationMeta.DIR_PATH_PARAM;
import static ru.slevyns.testtask.util.DirValidationMeta.MIN_WORD_PARAM;
import static ru.slevyns.testtask.util.DirValidationMeta.MUST_BE_GREATER_THAN_ERROR;
import static ru.slevyns.testtask.util.DirValidationMeta.TOP_N_PARAM;

@Order(5)
class DirValidationServiceTest {
    private final ValidationService<DirRequest> validationService = new DirValidationService();

    @Test
    void validateRequest_AllRequestParamsInvalid_ReturnAllErrors() {
        var request = new DirRequest("", -1, -1);
        var dirError = new ValidationResult(DIR_PATH_ERROR, DIR_PATH_PARAM);
        var minWordError = new ValidationResult(MUST_BE_GREATER_THAN_ERROR.formatted(MIN_WORD_PARAM), MIN_WORD_PARAM);
        var topNError = new ValidationResult(MUST_BE_GREATER_THAN_ERROR.formatted(TOP_N_PARAM), TOP_N_PARAM);

        var result = validationService.validate(request);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Set.of(dirError, minWordError, topNError)));
    }

    @Test
    void validateRequest_AllRequestParamsValid_ReturnZeroErrors() {
        var request = new DirRequest(CONTROLLER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);

        var result = validationService.validate(request);

        assertTrue(result.isEmpty());
    }
}