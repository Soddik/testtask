package ru.slevyns.testtask.service.strategy;

import ru.slevyns.testtask.dto.DbRequest;

/**
 * Service configures and execute strategy
 */
public interface StrategyService {
    /**
     * Execute strategy
     *
     * @param request request with parameters for deleting data
     * @return result of strategy execution
     */
    long execute(DbRequest request);
}
