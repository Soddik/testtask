package ru.slevyns.testtask.dto.dir;

import java.util.Objects;

public enum AllowedExtension {
    TXT("txt");
    public final String value;

    AllowedExtension(String value) {
        this.value = value;
    }

    public boolean isEqual(String str){
        return Objects.equals(value, str);
    }
}
