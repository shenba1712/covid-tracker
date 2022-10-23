package com.app.covidtracker.repository.custom;

import com.app.covidtracker.domain.Country;

import javax.validation.constraints.NotNull;

public interface CountryRepositoryCustom {
    void upsertCountry(@NotNull String countryName, Country country);
}
