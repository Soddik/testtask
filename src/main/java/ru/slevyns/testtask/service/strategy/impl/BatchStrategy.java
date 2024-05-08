package ru.slevyns.testtask.service.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slevyns.testtask.service.DbService;

import java.util.stream.IntStream;

import static ru.slevyns.testtask.util.QueryMeta.DELETE_DATA_BY_SUBQUERY;
import static ru.slevyns.testtask.util.QueryMeta.SELECT_COUNT_OF_RECORDS_BY_DATE;

/**
 * Batch deletion strategy
 * <pre>
 *      DELETE FROM %s WHERE id IN (SELECT id FROM %s WHERE date_time <= '%s' limit %d)
 * </pre>
 */
@Service(StrategyType.BATCH)
public class BatchStrategy extends BaseDeleteStrategy {
    private static final Logger log = LoggerFactory.getLogger(BatchStrategy.class);
    private static final double RECOMMENDED_PERCENT = 30;
    private static final Integer STEP_PERCENT = 10;

    public BatchStrategy(DbService dbService) {
        super(dbService);
    }

    @Override
    @Transactional
    public void delete(String tableName, String dateTime) {
        var rowsToDel = countRowsToDel(tableName, dateTime);

        if (rowsToDel == null || rowsToDel == 0) {
            log.info("Nothing to delete in table: {}", tableName);
            return;
        }

        var batchSize = rowsToDel / STEP_PERCENT;
        log.info("Table: {} contains {} records to delete", tableName, rowsToDel);

        var delQuery = getFormatedDelQuery(tableName, dateTime, batchSize);
        IntStream.rangeClosed(1, STEP_PERCENT)
                .forEach(i -> {
                    dbService.execute(delQuery);
                    var percent = String.valueOf((i * RECOMMENDED_PERCENT) / 3);
                    log.info("Deleted: {}%", percent);
                });

        log.info("Deletion completed");
    }

    @Override
    public boolean isOptimal(double percent) {
        return RECOMMENDED_PERCENT > percent;
    }

    @Override
    public String getType() {
        return StrategyType.BATCH;
    }

    private Integer countRowsToDel(String tableName, String dateTime) {
        return dbService.queryForObject(
                String.format(SELECT_COUNT_OF_RECORDS_BY_DATE, tableName, dateTime),
                Integer.class
        );
    }

    private String getFormatedDelQuery(String tableName, String dateTime, int limit) {
        return DELETE_DATA_BY_SUBQUERY.formatted(tableName, tableName, dateTime, limit);
    }
}
