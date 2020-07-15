package com.app.covidtracker.repository;

import com.app.covidtracker.domain.india.Resources;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResourcesRepository extends MongoRepository<Resources, String> {
    List<Resources> findByStateAndCityAllIgnoreCase(String state, String city);
    List<Resources> findByStateIgnoreCase(String state);
}
