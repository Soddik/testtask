package ru.slevyns.testtask.service;

import ru.slevyns.testtask.domain.WordCollection;

import java.nio.file.Path;

public interface WordCountService {
    WordCollection countWords(Path directoryPath);
}
