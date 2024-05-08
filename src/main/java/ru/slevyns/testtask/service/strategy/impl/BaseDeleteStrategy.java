package ru.slevyns.testtask.service.strategy.impl;

import ru.slevyns.testtask.service.DbService;
import ru.slevyns.testtask.service.strategy.DeleteStrategy;

public abstract class BaseDeleteStrategy implements DeleteStrategy {
    protected static final double MAX_PERCENT = 100;
    protected final DbService dbService;

    public BaseDeleteStrategy(DbService dbService) {
        this.dbService = dbService;
    }
}
