package com.app.covidtracker.repository;

import com.app.covidtracker.domain.india.State;
import com.app.covidtracker.repository.custom.IndianStatesRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndianStatesRepository extends MongoRepository<State, String>, IndianStatesRepositoryCustom {
    Optional<State> findByStateNameIgnoreCase(String name);
}
