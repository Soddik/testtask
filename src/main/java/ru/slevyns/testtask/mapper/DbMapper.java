package ru.slevyns.testtask.mapper;

import org.springframework.stereotype.Service;
import ru.slevyns.testtask.dto.ValidationResult;
import ru.slevyns.testtask.dto.DbRequest;
import ru.slevyns.testtask.dto.DbResponse;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for DB data deletion task
 */
@Service
public class DbMapper {
    public DbResponse toResponse(DbRequest request, long ms, long rowsDeleted) {
        var tableName = request.tableName();
        return new DbResponse(tableName, ms, rowsDeleted, Set.of());
    }

    public DbResponse toResponse(Set<ValidationResult> validationResults, DbRequest request) {
        var errors = validationResults.stream()
                .map(ValidationResult::toString)
                .collect(Collectors.toSet());
        return new DbResponse(
                request.tableName(),
                0,
                0,
                errors
        );
    }
}
