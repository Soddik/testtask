package ru.slevyns.testtask.mapper;

import org.springframework.stereotype.Service;
import ru.slevyns.testtask.domain.DirRequest;

import java.nio.file.Path;

@Service
public class DirMapper {
    public Path toDirectoryPath(DirRequest request) {
        return Path.of(request.dirPath());
    }
}
