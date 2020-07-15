package com.app.covidtracker.repository;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.repository.custom.CountryRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends MongoRepository<Country, String>, CountryRepositoryCustom {
    Optional<Country> findByCountryNameIgnoreCase(String countryName);
}
