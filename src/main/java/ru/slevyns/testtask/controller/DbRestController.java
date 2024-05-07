package ru.slevyns.testtask.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slevyns.testtask.dto.db.DbRequest;
import ru.slevyns.testtask.dto.db.DbResponse;
import ru.slevyns.testtask.dto.db.DpPopulateRequest;
import ru.slevyns.testtask.service.database.DeleteService;
import ru.slevyns.testtask.util.DbPopulationService;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/db/")
public class DbRestController {
    private static final Logger log = LoggerFactory.getLogger(DbRestController.class);
    private final DeleteService deleteService;
    private final DbPopulationService populationService;


    public DbRestController(DeleteService deleteService, DbPopulationService populationService) {
        this.deleteService = deleteService;
        this.populationService = populationService;
    }

    /**
     * Table row deletion endpoint
     *
     * @param request contains table name and date
     */
    @Operation(summary = "Delete old records from given table")
    @PostMapping("delete")
    public ResponseEntity<DbResponse> delete(@RequestBody DbRequest request) {
        try {
            var response = deleteService.deleteRowsByDate(request);
            var isValid = response.errors().isEmpty();
            return isValid
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            var response = new DbResponse(
                    request.tableName(),
                    0,
                    0,
                    Set.of(e.getMessage()));
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Test purpose only! Populates table with data. 10_000_000 rows
     *
     * @param request with table name
     */
    @Operation(summary = "!TEST! Populates given table with 10_000_000 records")
    @PostMapping("populate")
    public ResponseEntity<?> populate(@RequestBody DpPopulateRequest request) {
        populationService.populateViaApi(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
