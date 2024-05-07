package ru.slevyns.testtask.db;

import java.time.LocalDateTime;

public interface DbTestMeta {
    String POSTGRES_IMAGE = "postgres:14.6";
    String DB_NAME = "test_db";
    String DB_USER = "postgres";
    String DB_PASSWORD = "root";

    int TABLE_SIZE = 10_000_000;

    LocalDateTime TEST_DATE_TIME = LocalDateTime.now();

    String DELETE_API = "/api/v1/db/delete";
    String POPULATE_API = "/api/v1/db/populate";
}
