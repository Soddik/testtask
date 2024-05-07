package ru.slevyns.testtask.service.word_counter;

import ru.slevyns.testtask.dto.dir.DirRequest;
import ru.slevyns.testtask.dto.dir.DirResponse;

public interface WordCountService {
    /**
     * Handle request and count words
     *
     * @param request params for directory
     * @return a Set of filtered words from files
     */
    DirResponse countWords(DirRequest request);
}
