package com.app.covidtracker.repository.custom;

import com.app.covidtracker.domain.india.District;
import com.app.covidtracker.domain.india.State;

import javax.validation.constraints.NotNull;

public interface IndianStatesRepositoryCustom {
    void upsertState(@NotNull String stateName, State state);
}
