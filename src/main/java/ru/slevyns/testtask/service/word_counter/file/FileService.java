package ru.slevyns.testtask.service.word_counter.file;


import ru.slevyns.testtask.domain.Word;

import java.nio.file.Path;
import java.util.Set;

public interface FileService {
    /**
     * Process all valid files in input directory
     *
     * @param directoryPath path of input directory
     * @return a Set of all words in file that matches regEx
     */
    Set<Word> processFiles(Path directoryPath);

    /**
     * Changes min word length for regEx
     *
     * @param minWordLength new min word length for regEx
     */
    void changeMinWordLength(int minWordLength);
}
