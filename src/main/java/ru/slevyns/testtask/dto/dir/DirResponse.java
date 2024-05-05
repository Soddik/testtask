package ru.slevyns.testtask.dto.dir;

import ru.slevyns.testtask.dto.ValidationResult;

import java.util.Set;

public record DirResponse (Set<Word> words, Set<ValidationResult> errors){
}
