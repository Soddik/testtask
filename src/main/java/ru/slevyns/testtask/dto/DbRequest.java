package ru.slevyns.testtask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for rows deletion")
public record DbRequest(
        @Schema(description = "Table name")
        String tableName,
        @Schema(description = "Upper date limit")
        String dateTime) {
}
