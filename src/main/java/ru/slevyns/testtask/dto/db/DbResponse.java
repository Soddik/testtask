package ru.slevyns.testtask.dto.db;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Rows deletion response")
public record DbResponse(
        @Schema(description = "Table name")
        String tableName,
        @Schema(description = "Execution time in ms")
        long ms,
        @Schema(description = "Rows deleted")
        long rowsDeleted,
        @Schema(description = "Errors")
        Set<String> errors) {
}
