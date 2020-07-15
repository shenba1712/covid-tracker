package com.app.covidtracker.service;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.domain.GlobalTimeSeries;
import com.app.covidtracker.domain.frontend.ChartObject;

import java.util.List;

public interface GlobalStatsService {
    void updateAllCountryStats();
    List<Country> getAllCountryStats();

    void updateGlobalStats();
    GlobalTimeSeries getLatestGlobalStats();

    Country getCountryStats(String countryName);
    List<ChartObject> getGlobalChartData();
    List<ChartObject> getCountryChartData(String country);
}
