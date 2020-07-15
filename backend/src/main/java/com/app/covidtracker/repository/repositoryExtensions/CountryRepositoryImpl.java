package com.app.covidtracker.repository.repositoryExtensions;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.repository.custom.CountryRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class CountryRepositoryImpl implements CountryRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Override
    public void upsertCountry(String countryName, Country country) {
        Query query = Query.query(where("countryName").is(countryName));
        Update update = new Update();
        update.set("timeSeries", country.getTimeSeries());
        update.set("total", country.getTotal());
        update.set("lastModifiedOn", LocalDateTime.now());
        operations.upsert(query, update, Country.class);
    }
}
