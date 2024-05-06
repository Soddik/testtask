package ru.slevyns.testtask.service.database.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.slevyns.testtask.dto.db.DbRequest;
import ru.slevyns.testtask.service.database.DbService;
import ru.slevyns.testtask.service.database.strategy.DeleteStrategy;
import ru.slevyns.testtask.service.database.strategy.StrategyFactory;
import ru.slevyns.testtask.service.database.strategy.StrategyService;

import static ru.slevyns.testtask.util.QueryMeta.SELECT_COUNT_OF_RECORDS_BY_DATE;
import static ru.slevyns.testtask.util.QueryMeta.SELECT_COUNT_ROWS_BY_TABLE_NAME;

@Service
public class StrategyServiceImpl implements StrategyService {
    private static final int MAX_PERCENT = 100;
    private static final Logger log = LoggerFactory.getLogger(StrategyServiceImpl.class);
    private final StrategyFactory factory;
    private final DbService dbService;
    private DeleteStrategy selectedStrategy;

    public StrategyServiceImpl(StrategyFactory factory, DbService dbService) {
        this.factory = factory;
        this.dbService = dbService;
    }

    @Override
    public long execute(DbRequest request) {
        var tableName = request.tableName();
        var dateTime = request.dateTime();
        var countOfRecordsToRemove = countRowsToDelete(tableName, dateTime);
        log.info("Percentage of records to be deleted: {}", countOfRecordsToRemove);

        var rowsTotal = countRowsTotal(tableName);
        selectOptimal(countOfRecordsToRemove, rowsTotal);
        selectedStrategy.delete(tableName, dateTime);
        return countOfRecordsToRemove;
    }

    /**
     * Select optimal deletion strategy based on the percentage of rows to be deleted to the number of all rows in the table
     *
     * @param removeRowsCount number of rows for deletion
     * @param rowsTotal       table total rows number
     */
    private void selectOptimal(Integer removeRowsCount, Integer rowsTotal) {
        double percent = calculatePercentage(removeRowsCount, rowsTotal);
        this.selectedStrategy = factory.getStrategies().stream()
                .filter(removeStrategy -> removeStrategy.isOptimal(percent))
                .peek(strategy -> log.info("Strategy selected: {}", strategy.getType()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Deletion strategy not found"));
    }

    private double calculatePercentage(double obtained, double total) {
        return obtained * MAX_PERCENT / total;
    }

    private Integer countRowsTotal(String table) {
        return dbService.queryForObject(
                String.format(SELECT_COUNT_ROWS_BY_TABLE_NAME, table),
                Integer.class);
    }

    private Integer countRowsToDelete(String table, String date) {
        var counted = dbService.queryForObject(
                String.format(SELECT_COUNT_OF_RECORDS_BY_DATE, table, date),
                Integer.class);
        return counted != null
                ? counted
                : 0;
    }
}
