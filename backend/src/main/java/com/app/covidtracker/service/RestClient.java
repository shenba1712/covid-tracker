package com.app.covidtracker.service;

import java.util.Optional;

public interface RestClient {
    Optional<String> getGlobalStats();
    Optional<String> getIndiaResources();
    Optional<String> getIndianStatsStats();
    Optional<String> getIndianZones();
}