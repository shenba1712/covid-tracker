package com.app.covidtracker.controller;

import com.app.covidtracker.domain.frontend.ChartObject;
import com.app.covidtracker.domain.frontend.FrontendState;
import com.app.covidtracker.domain.frontend.FrontendDistrict;
import com.app.covidtracker.domain.india.District;
import com.app.covidtracker.domain.india.Resources;
import com.app.covidtracker.domain.india.State;
import com.app.covidtracker.service.IndianStatsService;
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
@RequestMapping(value = "/indian")
public class IndianStatsRestController {

    @Autowired
    private MapperService mapperService;

    @Autowired
    private IndianStatsService indianStatsService;

    @ApiOperation("Get statistics of all states")
    @GetMapping("/states")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FrontendState> geAlltStatesStats() {
        return mapperService.mapToFrontendStates(indianStatsService.getAllIndianStatesStats());
    }

    @ApiOperation("Get statistics of an indian state")
    @GetMapping("/state/{name}")
    public ResponseEntity<FrontendState> getStateStats(@ApiParam("indian state name") @PathVariable("name") String stateName) {
        State state = indianStatsService.getStateStats(stateName);
        if (nonNull(state)) {
            return ResponseEntity.ok(mapperService.mapToFrontendState(state));
        } else {
            log.warn("Not data found for state {}", stateName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Get statistics of an indian district")
    @GetMapping("/state/{stateName}/district/{districtName}")
    public ResponseEntity<FrontendDistrict> getDistrictStats(@ApiParam("State name") @PathVariable("stateName") String stateName,
                                                  @ApiParam("District name") @PathVariable("districtName") String districtName) {
        District district = indianStatsService.getDistrictStats(stateName, districtName);
        if (nonNull(district)) {
            return ResponseEntity.ok(mapperService.mapToFrontendDistrict(district));
        } else {
            log.warn("Not data found for district {} of state {}", districtName, stateName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Api to get important resources")
    @GetMapping("/resources")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Resources> getResources(@ApiParam("district name") @RequestParam(value = "districtName", required = false) String districtName,
                                        @ApiParam("state name") @RequestParam("stateName") @NotNull String stateName) {
        if (nonNull(districtName)) {
            return indianStatsService.getResources(stateName, districtName);
        } else {
            return indianStatsService.getResources(stateName);
        }
    }

    @GetMapping("/chart")
    @ResponseStatus(HttpStatus.OK)
    public List<ChartObject> getChartData(@RequestParam(value = "state") String state,
                                          @RequestParam(value = "district", required = false) String district) {
        return indianStatsService.getChartData(state, district);
    }

    @ApiOperation("Update resources")
    @GetMapping("/update-resources")
    @ResponseStatus(HttpStatus.OK)
    public void updateResources() throws Exception {
        indianStatsService.updateIndianResources();
    }

    @ApiOperation("Update state statistics")
    @GetMapping("/update-states")
    @ResponseStatus(HttpStatus.OK)
    public void updateStates() throws Exception {
        indianStatsService.updateIndianStatesStats();
    }

    @ApiOperation("Update zones")
    @GetMapping("/update-zones")
    @ResponseStatus(HttpStatus.OK)
    public void updateZones() throws Exception {
        indianStatsService.updateIndianZones();
    }
}
