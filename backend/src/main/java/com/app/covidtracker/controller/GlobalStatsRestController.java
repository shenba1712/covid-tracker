package com.app.covidtracker.controller;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.domain.GlobalTimeSeries;
import com.app.covidtracker.domain.frontend.ChartObject;
import com.app.covidtracker.domain.frontend.FrontendCountry;
import com.app.covidtracker.service.GlobalStatsService;
import com.app.covidtracker.serviceImpl.MapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/global")
public class GlobalStatsRestController {

    @Autowired
    private GlobalStatsService globalStatsService;

    @Autowired
    private MapperService mapperService;

    @GetMapping("/latest")
    @ResponseStatus(HttpStatus.OK)
    public GlobalTimeSeries getGlobalStats() {
        return globalStatsService.getLatestGlobalStats();
    }

    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<FrontendCountry> getCountryStats() {
       return mapperService.mapToFrontendCountry(globalStatsService.getAllCountryStats());
    }

    @GetMapping("/country/{name}")
    public ResponseEntity<Country> getCountryStats(@PathVariable("name") @NotNull String countryName) {
        Country country = globalStatsService.getCountryStats(countryName);
        if (nonNull(country)) {
            return ResponseEntity.ok(country);
        } else {
            log.warn("Not data found for country {}", countryName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/chart")
    @ResponseStatus(HttpStatus.OK)
    public List<ChartObject> getChartData(@RequestParam(value = "country", required = false) String country) {
        if (nonNull(country)) {
            return globalStatsService.getCountryChartData(country);
        } else {
            return globalStatsService.getGlobalChartData();
        }
    }

}
