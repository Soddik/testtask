package ru.slevyns.testtask.util;

public interface DbValidationMeta {
    String TABLE_NAME_PARAM = "tableName";
    String DATE_TIME_PARAM = "dateTime";

    String EMPTY_ERROR = "%s cannot be empty";
    String REGEX_NOT_MATCH_ERROR = "%s must match regex: %s";

}
