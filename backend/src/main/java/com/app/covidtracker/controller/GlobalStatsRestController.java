package com.app.covidtracker.controller;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.domain.GlobalTimeSeries;
import com.app.covidtracker.domain.frontend.ChartObject;
import com.app.covidtracker.domain.frontend.FrontendCountry;
import com.app.covidtracker.service.GlobalStatsService;
import com.app.covidtracker.serviceImpl.MapperService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation("Get latest global stats")
    @GetMapping("/latest")
    @ResponseStatus(HttpStatus.OK)
    public GlobalTimeSeries getGlobalStats() {
        return globalStatsService.getLatestGlobalStats();
    }

    @ApiOperation("Get stats for all countries")
    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<FrontendCountry> getCountryStats() {
       return mapperService.mapToFrontendCountries(globalStatsService.getAllCountryStats());
    }

    @ApiOperation("Get stats for a country")
    @GetMapping("/country/{name}")
    public ResponseEntity<FrontendCountry> getCountryStats(@ApiParam("Country name") @PathVariable("name") @NotNull String countryName) {
        Country country = globalStatsService.getCountryStats(countryName);
        if (nonNull(country)) {
            return ResponseEntity.ok(mapperService.mapToFrontendCountry(country));
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

    @ApiOperation("Update countries statistics")
    @GetMapping("/update-countries")
    @ResponseStatus(HttpStatus.OK)
    public void updateCountryStats() {
        globalStatsService.updateAllCountryStats();
    }

    @ApiOperation("Update global statistics")
    @GetMapping("/update-global")
    @ResponseStatus(HttpStatus.OK)
    public void updateGlobalStats() {
        globalStatsService.updateGlobalStats();
    }

}
