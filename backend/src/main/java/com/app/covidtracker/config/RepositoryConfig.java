package com.app.covidtracker.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.app.covidtracker.repository")
public class RepositoryConfig {
}
