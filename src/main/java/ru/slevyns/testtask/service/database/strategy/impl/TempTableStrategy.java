package ru.slevyns.testtask.service.database.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slevyns.testtask.service.database.DbService;

import static ru.slevyns.testtask.util.QueryMeta.CREATE_TEMP_TABLE;
import static ru.slevyns.testtask.util.QueryMeta.DROP_TABLE_BY_NAME;
import static ru.slevyns.testtask.util.QueryMeta.INSERT_ROW_BY_SELECT;
import static ru.slevyns.testtask.util.QueryMeta.RENAME_TEMP_TABLE;

/**
 * Deletion strategy using temporary table
 */
@Service(StrategyType.TEMP_TABLE)
public class TempTableStrategy extends BaseDeleteStrategy {
    private static final Logger log = LoggerFactory.getLogger(TempTableStrategy.class);
    private static final double RECOMMENDED_PERCENT = 30;

    public TempTableStrategy(DbService dbService) {
        super(dbService);
    }

    @Override
    @Transactional
    public void delete(String tableName, String dateTime) {
        dbService.execute(CREATE_TEMP_TABLE.formatted(tableName));
        dbService.execute(INSERT_ROW_BY_SELECT.formatted(tableName, tableName, dateTime));
        dbService.execute(DROP_TABLE_BY_NAME.formatted(tableName));
        dbService.execute(RENAME_TEMP_TABLE.formatted(tableName, tableName));
        log.info("Deletion completed");
    }

    @Override
    public boolean isOptimal(double percent) {
        return percent >= RECOMMENDED_PERCENT && percent < MAX_PERCENT;
    }

    @Override
    public String getType() {
        return StrategyType.TEMP_TABLE;
    }
}
