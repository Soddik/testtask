package ru.slevyns.testtask.service.filter;

import ru.slevyns.testtask.domain.Word;

import java.util.Set;

public interface FilterService {
    int DEFAULT_TOP_NUM = 10;

    /**
     * Filters all words by topN
     *
     * @param words all words from files that match regEx
     * @return a Set of topN words
     */
    Set<Word> filter(Set<Word> words);

    /**
     * Changes topN quantity
     *
     * @param topWordsNum new topN quantity value     *
     */
    void changeTopWordsNum(int topWordsNum);
}
