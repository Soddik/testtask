package ru.slevyns.testtask.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.slevyns.testtask.dto.db.DbRequest;
import ru.slevyns.testtask.dto.db.DbResponse;
import ru.slevyns.testtask.dto.db.DpPopulateRequest;
import ru.slevyns.testtask.service.database.DbService;
import ru.slevyns.testtask.util.DbPopulationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.utility.DockerImageName.parse;
import static ru.slevyns.testtask.db.DbTestMeta.*;
import static ru.slevyns.testtask.util.DbPopulationService.TEST_TABLE_NAME;
import static ru.slevyns.testtask.util.QueryMeta.*;

@ExtendWith(SpringExtension.class)
@Testcontainers
@WebAppConfiguration
@SpringBootTest
class DbRestControllerTest {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    protected WebApplicationContext context;
    protected MockMvc mockMvc;
    @SpyBean
    protected DbPopulationService populationService;
    @SpyBean
    protected DbService dbService;

    @ClassRule
    @Container
    public static final PostgreSQLContainer<?> SQL_CONTAINER = new PostgreSQLContainer<>(parse(POSTGRES_IMAGE))
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USER)
            .withPassword(DB_PASSWORD);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", SQL_CONTAINER::getPassword);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void tearDown() {
        populationService.dropTable(TEST_TABLE_NAME);
    }

    @Test
    public void deleteRows_validRequestBatchStrategy_success() throws Exception {
        populateDbForBatchAndTempStrategy();
        var testDate = dtf.format(TEST_DATE_TIME.plusDays(1));
        var request = new DbRequest(TEST_TABLE_NAME, testDate);

        var response = performOkPost(request);

        var rowsDeleted = response.rowsDeleted();
        var errors = response.errors();

        assertTrue(rowsDeleted < TABLE_SIZE * 0.3);
        assertEquals(0, errors.size());
    }

    @Test
    public void deleteRows_validRequestBatchStrategy_NothingToDelete() throws Exception {
        populateDbForBatchAndTempStrategy();
        var testDate = dtf.format(TEST_DATE_TIME);
        var request = new DbRequest(TEST_TABLE_NAME, testDate);

        var response = performOkPost(request);

        var rowsDeleted = response.rowsDeleted();
        var errors = response.errors();

        assertEquals(0, rowsDeleted);
        assertEquals(0, errors.size());
    }

    @Test
    public void deleteRows_validRequestTempTableStrategy_success() throws Exception {
        populateDbForBatchAndTempStrategy();
        var testDate = dtf.format(TEST_DATE_TIME.plusWeeks(7));
        var request = new DbRequest(TEST_TABLE_NAME, testDate);

        var response = performOkPost(request);

        var rowsDeleted = response.rowsDeleted();
        var errors = response.errors();

        assertTrue((rowsDeleted >= TABLE_SIZE * 0.3) && rowsDeleted < TABLE_SIZE);
        assertEquals(0, errors.size());
    }

    @Test
    public void deleteRows_validRequestTruncStrategy_success() throws Exception {
        populateDbForTruncStrategy();
        var testDate = dtf.format(TEST_DATE_TIME.plusDays(1));
        var request = new DbRequest(TEST_TABLE_NAME, testDate);

        var response = performOkPost(request);

        var rowsDeleted = response.rowsDeleted();
        var errors = response.errors();

        assertEquals(TABLE_SIZE, rowsDeleted);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void deleteRows_invalidRequest_fail() throws Exception {
        var request = new DbRequest(null, null);

        var mvcResult = mockMvc.perform(post(DELETE_API)
                        .content(asJsonString(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        var response = getObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                DbResponse.class
        );

        var errors = response.errors();


        assertEquals(2, errors.size());
    }

    @Test
    public void populateDb() throws Exception {
        var request = new DpPopulateRequest(TEST_TABLE_NAME);
        mockMvc.perform(post(POPULATE_API)
                        .content(asJsonString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        var totalRows = dbService.queryForObject(SELECT_COUNT_ROWS_BY_TABLE_NAME.formatted(TEST_TABLE_NAME), Integer.class);
        assertEquals(10_000_000, totalRows);
    }

    private DbResponse performOkPost(DbRequest request) throws Exception {
        var mvcResult = mockMvc.perform(post(DELETE_API)
                        .content(asJsonString(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return getObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                DbResponse.class
        );
    }

    private void populateDbForBatchAndTempStrategy() {
        populationService.createTable(TEST_TABLE_NAME);
        var now = LocalDateTime.now();
        populationService.generateRows(TEST_TABLE_NAME, TABLE_SIZE, now::plusSeconds);
    }

    private void populateDbForTruncStrategy() {
        populationService.createTable(TEST_TABLE_NAME);
        var now = LocalDateTime.now();
        populationService.generateRows(TEST_TABLE_NAME, TABLE_SIZE, (i) -> now);
    }

    private static String asJsonString(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}