package ru.slevyns.testtask.util;

/**
 * SQL queries
 */
public interface QueryMeta {
    String SELECT_COUNT_OF_RECORDS_BY_DATE = "SELECT COUNT(*) FROM %s WHERE date_time <= '%s'";
    String DELETE_DATA_BY_SUBQUERY = "DELETE FROM %s WHERE id IN (SELECT id FROM %s WHERE date_time <= '%s' limit %d)";
    String DROP_TABLE_BY_NAME = "DROP TABLE IF EXISTS %s";
    String SELECT_COUNT_ROWS_BY_TABLE_NAME = "SELECT COUNT(*) FROM %s";
    String TRUNCATE_TABLE_BY_NAME = "TRUNCATE %s";
    String CREATE_TEMP_TABLE = "CREATE TABLE IF NOT EXISTS temp_%s (id SERIAL PRIMARY KEY, date_time timestamp)";
    String INSERT_ROW_BY_SELECT = "INSERT INTO temp_%s (id,date_time) SELECT * FROM %s WHERE date_time > '%s'";
    String RENAME_TEMP_TABLE = "ALTER TABLE temp_%s RENAME to %s";
    String SET_LAST_INDEX = "SELECT SETVAL((SELECT PG_GET_SERIAL_SEQUENCE('%s', 'id')), (SELECT (MAX(id) + 1) FROM %s), FALSE);";
    String CREATE_TABLE_BY_NAME = "CREATE TABLE IF NOT EXISTS %s (id SERIAL PRIMARY KEY, date_time timestamp)";
}
