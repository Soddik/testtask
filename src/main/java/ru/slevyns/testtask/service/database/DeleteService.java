package ru.slevyns.testtask.service.database;

import ru.slevyns.testtask.dto.db.DbRequest;
import ru.slevyns.testtask.dto.db.DbResponse;

/**
 * Service for deleting old records from table
 */
public interface DeleteService {
    /**
     * Deletes records from the table in accordance with the request parameters
     *
     * @param request request with parameters for deleting data
     * @return information about the delete operation
     */
    DbResponse deleteRowsByDate(DbRequest request);
}
