package ru.slevyns.testtask.mapper;

import org.springframework.stereotype.Service;
import ru.slevyns.testtask.dto.dir.DirRequest;
import ru.slevyns.testtask.dto.dir.DirResponse;
import ru.slevyns.testtask.dto.dir.ValidationResult;
import ru.slevyns.testtask.dto.dir.Word;

import java.nio.file.Path;
import java.util.Set;

@Service
public class DirMapper {
    public Path toDirectoryPath(DirRequest request) {
        return Path.of(request.dirPath());
    }

    public DirResponse toResponse(Set<Word> words, Set<ValidationResult> validationResult) {
        return new DirResponse(words, validationResult);
    }
}
