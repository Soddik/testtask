package ru.slevyns.testtask.service.database.strategy;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class StrategyFactory {
    private final Map<String, DeleteStrategy> strategies;

    public StrategyFactory(Map<String, DeleteStrategy> strategies) {
        this.strategies = strategies;
    }

    public Set<DeleteStrategy> getStrategies() {
        return new HashSet<>(strategies.values());
    }
}
