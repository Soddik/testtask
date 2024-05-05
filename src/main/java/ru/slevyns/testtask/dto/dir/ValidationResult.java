package ru.slevyns.testtask.dto.dir;

public record ValidationResult(String errorMessage, String paramName) {
    @Override
    public String toString() {
        return "param: " + paramName + ", error: " + errorMessage;
    }
}
