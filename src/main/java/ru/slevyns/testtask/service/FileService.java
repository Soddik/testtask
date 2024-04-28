package ru.slevyns.testtask.service;


import java.nio.file.Path;

public interface FileService<T> {
    T processFiles(Path directoryPath);
}
