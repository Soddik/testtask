package ru.slevyns.testtask.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.slevyns.testtask.dto.DbRequest;
import ru.slevyns.testtask.dto.DbResponse;
import ru.slevyns.testtask.mapper.DbMapper;
import ru.slevyns.testtask.service.DeleteService;
import ru.slevyns.testtask.service.strategy.StrategyService;
import ru.slevyns.testtask.service.validation.ValidationService;

@Service
public class DeleteServiceImpl implements DeleteService {
    private static final Logger log = LoggerFactory.getLogger(DeleteServiceImpl.class);
    private final ValidationService<DbRequest> validationService;
    private final StrategyService strategyService;
    private final DbMapper mapper;

    public DeleteServiceImpl(ValidationService<DbRequest> validationService, StrategyService strategyService, DbMapper mapper) {
        this.validationService = validationService;
        this.strategyService = strategyService;
        this.mapper = mapper;
    }


    @Override
    public DbResponse deleteRowsByDate(DbRequest request) {
        var errors = validationService.validate(request);

        if (!errors.isEmpty()) {
            return mapper.toResponse(errors, request);
        }

        log.info("Processing deletion...");
        var watch = new StopWatch();
        watch.start();
        var rowsDeleted = strategyService.execute(request);
        watch.stop();

        var totalTimeMillis = watch.getTotalTimeMillis();
        log.info("Deletion completed in {} ms", totalTimeMillis);
        return mapper.toResponse(request, totalTimeMillis, rowsDeleted);
    }
}
