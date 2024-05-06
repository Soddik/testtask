package ru.slevyns.testtask.service.database;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Database interaction service
 */
@Service
public interface DbService {
    /**
     * Returns data source bean
     *
     * @return dataSource
     */
    DataSource getDataSource();

    /**
     * Executes SQL query
     *
     * @param sql SQL query
     */
    void execute(String sql);

    /**
     * Executes SQL query. Casts the return value specified to the type.
     *
     * @param sql       SQL query
     * @param className output object Class
     * @param <T>       return value
     * @return the query result converted into className
     */
    <T> T queryForObject(String sql, Class<? extends T> className);

}
