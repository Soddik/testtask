package ru.slevyns.testtask.domain;

import java.util.Collections;
import java.util.List;

public class WordCollection {
    private final List<Word> words;

    public WordCollection(List<Word> words) {
        this.words = words;
    }

    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }

    public void add(Word word) {
        words.add(word);
    }

    @Override
    public String toString() {
        return "WordCollection{" +
                "words=" + words +
                '}';
    }
}
