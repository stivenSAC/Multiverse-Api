package com.example.Multiverse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DbController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/db-check")
    public Map<String, Object> checkDb() {
        return jdbcTemplate.queryForMap("SELECT version()");
    }
}
