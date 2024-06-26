package ru.slevyns.testtask.dir;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.slevyns.testtask.controller.DirectoryController;
import ru.slevyns.testtask.dto.dir.DirRequest;
import ru.slevyns.testtask.dto.dir.DirResponse;
import ru.slevyns.testtask.dto.ValidationResult;
import ru.slevyns.testtask.dto.dir.Word;
import ru.slevyns.testtask.service.word_counter.WordCountService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static ru.slevyns.testtask.dir.DirTestMeta.CONTROLLER_TEST_DIR_PATH;
import static ru.slevyns.testtask.dir.DirTestMeta.ERROR_REDIRECT;
import static ru.slevyns.testtask.dir.DirTestMeta.FIND_WORDS_PAGE;
import static ru.slevyns.testtask.dir.DirTestMeta.MIN_LENGTH;
import static ru.slevyns.testtask.dir.DirTestMeta.SUCCESS_REDIRECT;
import static ru.slevyns.testtask.dir.DirTestMeta.TOP_NUM;
import static ru.slevyns.testtask.dir.DirTestMeta.WORD_1;
import static ru.slevyns.testtask.dir.DirTestMeta.WORD_2;
import static ru.slevyns.testtask.util.DirValidationMeta.DIR_PATH_ERROR;
import static ru.slevyns.testtask.util.DirValidationMeta.DIR_PATH_PARAM;
import static ru.slevyns.testtask.util.DirValidationMeta.MIN_WORD_PARAM;
import static ru.slevyns.testtask.util.DirValidationMeta.MUST_BE_GREATER_THAN_ERROR;
import static ru.slevyns.testtask.util.DirValidationMeta.TOP_N_PARAM;

@ExtendWith(MockitoExtension.class)
class DirectoryControllerTest {
    @Mock
    private WordCountService wordCountService;

    @InjectMocks
    private DirectoryController dc;

    @Test
    void showWordsPage() {
        var result = dc.findWords();

        assertEquals(FIND_WORDS_PAGE, result);
    }

    @Test
    void findTopNWordsInDirectory_RequestIsValid_ReturnsRedirectionToResultPage() {
        var model = new ConcurrentModel();
        var request = new DirRequest(CONTROLLER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);

        var words = new HashSet<>(Set.of(new Word(WORD_1, 1), new Word(WORD_2, 2)));
        var response = new DirResponse(words, new HashSet<>());
        doReturn(response)
                .when(wordCountService)
                .countWords(request);

        var result = dc.findWords(model, request);

        assertEquals(SUCCESS_REDIRECT, result);

        verify(wordCountService).countWords(request);
        verifyNoMoreInteractions(wordCountService);
    }

    @Test
    void findTopNWordsInDirectory_RequestIsInvalid_ReturnsRedirectionToErrorPage() {
        var model = new ConcurrentModel();
        var request = new DirRequest("", -1, -1);

        var errors = fillErrors();

        var response = new DirResponse(null, errors);

        doReturn(response)
                .when(wordCountService)
                .countWords(request);

        var result = dc.findWords(model, request);
        assertEquals(ERROR_REDIRECT, result);

        verify(wordCountService).countWords(request);
        verifyNoMoreInteractions(wordCountService);
    }

    private Set<ValidationResult> fillErrors() {
        return Set.of(
                new ValidationResult(DIR_PATH_ERROR, DIR_PATH_PARAM),
                new ValidationResult(
                        MUST_BE_GREATER_THAN_ERROR.formatted(MIN_WORD_PARAM),
                        MIN_WORD_PARAM),
                new ValidationResult(
                        MUST_BE_GREATER_THAN_ERROR.formatted(TOP_N_PARAM),
                        TOP_N_PARAM)
        );
    }
}