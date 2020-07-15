package com.app.covidtracker.controller;

import com.app.covidtracker.domain.frontend.ChartObject;
import com.app.covidtracker.domain.frontend.FrontendState;
import com.app.covidtracker.domain.india.District;
import com.app.covidtracker.domain.india.Resources;
import com.app.covidtracker.domain.india.State;
import com.app.covidtracker.service.IndianStatsService;
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
@RequestMapping(value = "/indian")
public class IndianStatsRestController {

    @Autowired
    private MapperService mapperService;

    @Autowired
    private IndianStatsService indianStatsService;

    @GetMapping("/states")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FrontendState> geAlltStatesStats() {
        return mapperService.mapToFrontendState(indianStatsService.getAllIndianStatesStats());
    }

    @GetMapping("/state/{name}")
    public ResponseEntity<State> getStateStats(@PathVariable("name") String stateName) {
        State state = indianStatsService.getStateStats(stateName);
        if (nonNull(state)) {
            return ResponseEntity.ok(state);
        } else {
            log.warn("Not data found for state {}", stateName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/state/{stateName}/district/{districtName}")
    public ResponseEntity<District> getDistrictStats(@PathVariable("stateName") String stateName,
                                                  @PathVariable("districtName") String districtName) {
        District district = indianStatsService.getDistrictStats(stateName, districtName);
        if (nonNull(district)) {
            return ResponseEntity.ok(district);
        } else {
            log.warn("Not data found for district {} of state {}", districtName, stateName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/resources")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Resources> getResources(@RequestParam(value = "districtName", required = false) String districtName,
                                        @RequestParam("stateName") @NotNull String stateName) {
        if (nonNull(districtName)) {
            return indianStatsService.getResources(stateName, districtName);
        } else {
            return indianStatsService.getResources(stateName);
        }
    }

    @PutMapping("/geocode")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGeoLocation() {
        try {
            log.info("Start updating coordinates of resource locations");
            indianStatsService.updateGeoCode();
            log.info("Finish updating coordinates of resource locations");
        } catch (Exception e) {
            log.error("Error updating coordinates", e);
        }
    }

    @GetMapping("/chart")
    @ResponseStatus(HttpStatus.OK)
    public List<ChartObject> getChartData(@RequestParam(value = "state") String state,
                                          @RequestParam(value = "district", required = false) String district) {
        return indianStatsService.getChartData(state, district);
    }
}
