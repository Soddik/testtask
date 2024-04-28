package ru.slevyns.testtask.service.impl;

import ru.slevyns.testtask.domain.WordCollection;
import ru.slevyns.testtask.service.FileService;

import java.nio.file.Path;

public class WordCountFileServiceImpl implements FileService<WordCollection> {
    @Override
    public WordCollection processFiles(Path directoryPath) {
        return null;
    }
}
