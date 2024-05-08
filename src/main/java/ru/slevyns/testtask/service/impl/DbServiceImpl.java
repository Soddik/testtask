package ru.slevyns.testtask.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.slevyns.testtask.service.DbService;

import javax.sql.DataSource;

@Service
public class DbServiceImpl implements DbService {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public DbServiceImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }

    @Override
    public <T> T queryForObject(String sql, Class<? extends T> className) {
        return jdbcTemplate.queryForObject(sql, className);
    }
}
