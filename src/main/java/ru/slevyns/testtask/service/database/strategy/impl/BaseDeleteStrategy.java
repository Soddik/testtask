package ru.slevyns.testtask.service.database.strategy.impl;

import ru.slevyns.testtask.service.database.DbService;
import ru.slevyns.testtask.service.database.strategy.DeleteStrategy;

public abstract class BaseDeleteStrategy implements DeleteStrategy {
    protected static final double MAX_PERCENT = 100;
    protected final DbService dbService;

    public BaseDeleteStrategy(DbService dbService) {
        this.dbService = dbService;
    }
}
