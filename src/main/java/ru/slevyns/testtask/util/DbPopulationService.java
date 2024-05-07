package ru.slevyns.testtask.util;

import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slevyns.testtask.aspect.annotation.ExecutionTime;
import ru.slevyns.testtask.dto.db.DpPopulateRequest;
import ru.slevyns.testtask.service.database.DbService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static ru.slevyns.testtask.util.QueryMeta.CREATE_TABLE_BY_NAME;
import static ru.slevyns.testtask.util.QueryMeta.DROP_TABLE_BY_NAME;
import static ru.slevyns.testtask.util.QueryMeta.SET_LAST_INDEX;

/**
 * Utility service. Populates DB with 100_000_000 rows
 */
@Service
public class DbPopulationService {
    public static final String TEST_TABLE_NAME = "test_table";
    public static final String DATE_TIME_COLUMN_NAME = "date_time";
    public static final String ID_COLUMN_NAME = "id";
    private static final int BATCH_SIZE = 100_000;
    private final DbService dbService;

    public DbPopulationService(DbService dbService) {
        this.dbService = dbService;
    }

    /**
     * Create table
     *
     * @param table table name
     */
    @ExecutionTime
    @Transactional
    public void createTable(String table) {
        dbService.execute(CREATE_TABLE_BY_NAME.formatted(table));
    }

    /**
     * Add N rows in table
     *
     * @param table  table name
     * @param length number of rows
     */
    @ExecutionTime
    public void generateRows(String table, Integer length, Function<Integer, LocalDateTime> func) {
        var recordsBatch = new ArrayList<Map<String, LocalDateTime>>();
        IntStream.range(0, length)
                .mapToObj(i -> Collections.singletonMap(DATE_TIME_COLUMN_NAME, func.apply(i)))
                .forEach(item -> insertData(recordsBatch, table, item));

        if (!recordsBatch.isEmpty()) {
            executeBatch(table, recordsBatch);
        }
    }

    /**
     * Insert rows into the table in batches of 100_000
     *
     * @param batch     current batch
     * @param table     table name
     * @param insertRow row for addition
     */
    private void insertData(List<Map<String, LocalDateTime>> batch, String table, Map<String, LocalDateTime> insertRow) {
        if (batch.size() >= BATCH_SIZE) {
            executeBatch(table, batch);
            batch.clear();
        }

        batch.add(insertRow);
    }

    /**
     * Performs a batch insert of records into the specified table
     *
     * @param table table name
     * @param batch current batch
     */
    private void executeBatch(String table, List<Map<String, LocalDateTime>> batch) {
        dbService.execute(SET_LAST_INDEX.formatted(table, table));
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dbService.getDataSource())
                .withTableName(table)
                .usingGeneratedKeyColumns(ID_COLUMN_NAME)
                .usingColumns(DATE_TIME_COLUMN_NAME);
        simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(batch));
    }

    @ExecutionTime
    @Transactional
    public void dropTable(String tableName) {
        dbService.execute(DROP_TABLE_BY_NAME.formatted(tableName));
    }

    /**
     * Populate table with data. 10_000_000 rows.
     *
     * @param request populate db request
     */
    @ExecutionTime
    @Transactional
    public void populateViaApi(DpPopulateRequest request) {
        var tableName = request.tableName();
        dropTable(tableName);
        createTable(tableName);
        var now = LocalDateTime.now();
        generateRows(tableName, 10_000_000, now::plusSeconds);
    }
}
