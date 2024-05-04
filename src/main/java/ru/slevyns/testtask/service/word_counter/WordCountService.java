package ru.slevyns.testtask.service.word_counter;

import ru.slevyns.testtask.domain.DirRequest;
import ru.slevyns.testtask.domain.Word;

import java.util.Set;

public interface WordCountService {
    /**
     * Count words.
     * @param request params for directory
     * @return a Set of filtered words from files
     * */
    Set<Word> countWords(DirRequest request);
}
