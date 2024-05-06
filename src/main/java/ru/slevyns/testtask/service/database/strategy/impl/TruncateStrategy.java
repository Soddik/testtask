package ru.slevyns.testtask.service.database.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.slevyns.testtask.service.database.DbService;

import static ru.slevyns.testtask.util.QueryMeta.TRUNCATE_TABLE_BY_NAME;

/**
 * Truncate strategy
 */
@Service(StrategyType.TRUNC)
public class TruncateStrategy extends BaseDeleteStrategy {
    private static final Logger log = LoggerFactory.getLogger(TruncateStrategy.class);

    public TruncateStrategy(DbService dbService) {
        super(dbService);
    }

    @Override
    public void delete(String tableName, String dateTime) {
        dbService.execute(TRUNCATE_TABLE_BY_NAME.formatted(tableName));
        log.info("Deletion completed");
    }

    @Override
    public boolean isOptimal(double percent) {
        return MAX_PERCENT == percent;
    }

    @Override
    public String getType() {
        return StrategyType.TRUNC;
    }
}
