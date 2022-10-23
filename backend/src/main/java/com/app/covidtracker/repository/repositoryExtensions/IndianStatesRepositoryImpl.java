package com.app.covidtracker.repository.repositoryExtensions;

import com.app.covidtracker.domain.india.District;
import com.app.covidtracker.domain.india.State;
import com.app.covidtracker.repository.custom.IndianStatesRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class IndianStatesRepositoryImpl implements IndianStatesRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Autowired
    private MongoTemplate template;

    @Override
    public void upsertState(@NotNull String stateName, State state) {
        Query query = Query.query(where("stateName").is(stateName));
        Update update = new Update();
        update.set("stateTimeSeries", state.getStateTimeSeries());
        update.set("districts", state.getDistricts());
        update.set("total", state.getTotal());
        update.set("lastModifiedOn", LocalDateTime.now());
        operations.upsert(query, update, State.class);
    }
}
