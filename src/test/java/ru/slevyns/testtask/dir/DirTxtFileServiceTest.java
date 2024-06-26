package ru.slevyns.testtask.dir;

import org.junit.jupiter.api.Test;
import ru.slevyns.testtask.service.word_counter.file.impl.TxtFileService;
import ru.slevyns.testtask.service.word_counter.executor.CustomExecutorService;
import ru.slevyns.testtask.service.word_counter.file.FileService;
import ru.slevyns.testtask.service.word_counter.matcher.TextRegexMatcherService;
import ru.slevyns.testtask.service.word_counter.matcher.impl.TextRegexMatcherImpl;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static ru.slevyns.testtask.dir.DirTestMeta.*;

class DirTxtFileServiceTest {
    private final TextRegexMatcherService regexMatcherService = new TextRegexMatcherImpl();
    private final CustomExecutorService executorService = new CustomExecutorService();

    private final FileService fileService = new TxtFileService(regexMatcherService, executorService);

    @Test
    void processFiles_emptyFolder_ExceptionThrown() {
        var file = new File(EMPTY_DIR_PATH);
        var path = file.getAbsoluteFile().toPath();
        assertThrows(RuntimeException.class, () -> fileService.processFiles(path));
    }

    @Test
    void processFiles_notEmptyFolder_returnsAllWords() {
        var file = new File(COUNTER_TEST_DIR_PATH);
        var path = file.getAbsoluteFile().toPath();
        var result = fileService.processFiles(path);

        assertEquals(9, result.size());
    }

    @Test
    void processFiles_notEmptyFolderCustomMinWordLength_returnsAllWords(){
        var file = new File(COUNTER_TEST_DIR_PATH);
        var path = file.getAbsoluteFile().toPath();
        fileService.changeMinWordLength(6);
        var result = fileService.processFiles(path);

        assertEquals(3, result.size());
    }
}