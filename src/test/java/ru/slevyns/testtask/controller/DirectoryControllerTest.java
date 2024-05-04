package ru.slevyns.testtask.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.slevyns.testtask.domain.DirRequest;
import ru.slevyns.testtask.domain.ValidationResult;
import ru.slevyns.testtask.domain.Word;
import ru.slevyns.testtask.service.word_counter.WordCountService;
import ru.slevyns.testtask.service.validation.ValidationService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static ru.slevyns.testtask.meta.TestMeta.CONTROLLER_TEST_DIR_PATH;
import static ru.slevyns.testtask.meta.TestMeta.ERROR_REDIRECT;
import static ru.slevyns.testtask.meta.TestMeta.FIND_WORDS_PAGE;
import static ru.slevyns.testtask.meta.TestMeta.MIN_LENGTH;
import static ru.slevyns.testtask.meta.TestMeta.SUCCESS_REDIRECT;
import static ru.slevyns.testtask.meta.TestMeta.TOP_NUM;
import static ru.slevyns.testtask.meta.TestMeta.WORD_1;
import static ru.slevyns.testtask.meta.TestMeta.WORD_2;
import static ru.slevyns.testtask.util.DirValidationMeta.DIR_PATH_ERROR;
import static ru.slevyns.testtask.util.DirValidationMeta.DIR_PATH_PARAM;
import static ru.slevyns.testtask.util.DirValidationMeta.MIN_WORD_LENGTH_ERROR;
import static ru.slevyns.testtask.util.DirValidationMeta.MIN_WORD_PARAM;
import static ru.slevyns.testtask.util.DirValidationMeta.TOP_N_ERROR;
import static ru.slevyns.testtask.util.DirValidationMeta.TOP_N_PARAM;

@ExtendWith(MockitoExtension.class)
class DirectoryControllerTest {
    @Mock
    private WordCountService wordCountService;
    @Mock
    private ValidationService<DirRequest> validationService;

    @InjectMocks
    private DirectoryController dc;

    @Test
    void showWordsPage(){
        var result = dc.findWords();

        assertEquals(FIND_WORDS_PAGE, result);
    }

    @Test
    void findTopNWordsInDirectory_RequestIsValid_ReturnsRedirectionToResultPage() {
        var model = new ConcurrentModel();
        var request = new DirRequest(CONTROLLER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);

        var words = new HashSet<>(Set.of(new Word(WORD_1, 1), new Word(WORD_2, 2)));
        doReturn(words)
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
        var request = new DirRequest("", 4, 10);

        var errors = new HashSet<>(Set.of(
                new ValidationResult(DIR_PATH_ERROR, DIR_PATH_PARAM),
                new ValidationResult(MIN_WORD_LENGTH_ERROR, MIN_WORD_PARAM),
                new ValidationResult(TOP_N_ERROR, TOP_N_PARAM))
        );
        doReturn(errors)
                .when(validationService)
                .validate(request);

        var result = dc.findWords(model, request);
        assertEquals(ERROR_REDIRECT, result);

        verify(validationService).validate(request);
        verifyNoMoreInteractions(validationService);
    }
}