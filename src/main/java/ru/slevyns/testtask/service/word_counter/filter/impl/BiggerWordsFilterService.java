package ru.slevyns.testtask.service.word_counter.filter.impl;

import org.springframework.stereotype.Service;
import ru.slevyns.testtask.dto.dir.Word;
import ru.slevyns.testtask.service.word_counter.filter.FilterService;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BiggerWordsFilterService implements FilterService {
    private int changedWordsNum = 0;

    @Override
    public Set<Word> filter(Set<Word> words) {
        return words.stream()
                .sorted(Comparator.comparingInt(Word::count).reversed())
                .limit(selectWordsNum())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void changeTopWordsNum(int topWordsNum) {
        this.changedWordsNum = topWordsNum;
    }

    private int selectWordsNum() {
        return changedWordsNum > 0
                ? changedWordsNum
                : DEFAULT_TOP_NUM;
    }
}
