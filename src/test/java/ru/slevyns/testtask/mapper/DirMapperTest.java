package ru.slevyns.testtask.mapper;

import org.junit.jupiter.api.Test;
import ru.slevyns.testtask.dto.dir.DirRequest;
import ru.slevyns.testtask.dto.dir.DirResponse;
import ru.slevyns.testtask.dto.ValidationResult;
import ru.slevyns.testtask.dto.dir.Word;

import java.nio.file.Path;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static ru.slevyns.testtask.meta.TestMeta.CONTROLLER_TEST_DIR_PATH;
import static ru.slevyns.testtask.meta.TestMeta.MIN_LENGTH;
import static ru.slevyns.testtask.meta.TestMeta.TOP_NUM;

class DirMapperTest {
    private final DirMapper mapper = new DirMapper();

    @Test
    void toDirectoryPath() {
        var request = new DirRequest(CONTROLLER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);
        var directoryPath = mapper.toDirectoryPath(request);
        assertEquals(Path.of(CONTROLLER_TEST_DIR_PATH), directoryPath);
    }

    @Test
    void toResponse() {
        var words = new HashSet<Word>();
        var validationResult = new HashSet<ValidationResult>();
        var response = mapper.toResponse(words, validationResult);
        var expected = new DirResponse(words, validationResult);
        assertEquals(expected, response);
    }
}