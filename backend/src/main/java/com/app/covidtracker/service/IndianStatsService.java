package com.app.covidtracker.service;

import com.app.covidtracker.domain.frontend.ChartObject;
import com.app.covidtracker.domain.india.District;
import com.app.covidtracker.domain.india.Resources;
import com.app.covidtracker.domain.india.State;

import java.util.List;

public interface IndianStatsService {
    void updateIndianResources() throws Exception;
    void updateIndianStatesStats() throws Exception;
    void updateIndianZones() throws Exception;

    List<State> getAllIndianStatesStats();
    State getStateStats(String stateName);
    District getDistrictStats(String stateName, String districtName);

    List<Resources> getResources(String stateName, String districtName);
    List<Resources> getResources(String stateName);

    List<ChartObject> getChartData(String state, String district);
}
