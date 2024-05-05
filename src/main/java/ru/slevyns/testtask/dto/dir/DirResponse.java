package ru.slevyns.testtask.dto.dir;

import java.util.Set;

public record DirResponse (Set<Word> words, Set<ValidationResult> errors){
}
