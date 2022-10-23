package com.app.covidtracker.repository;

import com.app.covidtracker.domain.GlobalTimeSeries;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface GlobalTimeSeriesRepository extends MongoRepository<GlobalTimeSeries, String> {
    GlobalTimeSeries findByDate(LocalDate date);
    GlobalTimeSeries findFirstByOrderByDateDesc();
}
