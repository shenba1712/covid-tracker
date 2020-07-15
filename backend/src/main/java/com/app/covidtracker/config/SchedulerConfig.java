package com.app.covidtracker.config;

import com.mongodb.MongoClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT15M")
public class SchedulerConfig {

    @Autowired
    private MongoClientConfiguration mongoClientConfiguration;

    @Autowired
    @Qualifier("mongoClient")
    private MongoClient mongoClient;

    @Bean
    public LockProvider lockProvider() {
        return new MongoLockProvider(mongoClient.getDatabase(mongoClientConfiguration.getDatabaseName()));
    }
}
