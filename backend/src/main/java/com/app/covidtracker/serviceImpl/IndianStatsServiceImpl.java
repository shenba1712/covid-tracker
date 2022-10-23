package com.app.covidtracker.serviceImpl;

import com.app.covidtracker.domain.Delta;
import com.app.covidtracker.domain.TimeSeries;
import com.app.covidtracker.domain.frontend.ChartObject;
import com.app.covidtracker.domain.frontend.FrontendChartData;
import com.app.covidtracker.domain.india.District;
import com.app.covidtracker.domain.india.Resources;
import com.app.covidtracker.domain.india.State;
import com.app.covidtracker.repository.IndianStatesRepository;
import com.app.covidtracker.repository.ResourcesRepository;
import com.app.covidtracker.service.IndianStatsService;
import com.app.covidtracker.service.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class IndianStatsServiceImpl implements IndianStatsService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private IndianStatesRepository indianStatesRepository;

    @Autowired
    private ObjectMapper mapper;


    @Override
    public void updateIndianResources() throws Exception {
        String json = restClient.getIndiaResources().orElse(null);
        if (nonNull(json)) {
            List<Resources> resources = mapJsonToResources(json);
            if (resources.size() > resourcesRepository.count() ||
                    Math.subtractExact(resourcesRepository.count(), resources.size()) > 25) {
                resourcesRepository.deleteAll();
                resourcesRepository.saveAll(resources);
            }
        }
    }

    private List<Resources> mapJsonToResources(String json) {
        mapper.findAndRegisterModules();
        try {
            Map<String, List<Map<String, String>>> map = mapper.readValue(json, Map.class);
            if (map.size() > 0) {
                return map.get("resources").stream()
                        .map(value -> mapper.convertValue(value, Resources.class))
                        .collect(Collectors.toList());
            } else {
                log.error("Error loading resources list");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void updateIndianStatesStats() throws Exception {
        String json = restClient.getIndianStatsStats().orElse(null);
        if (nonNull(json)) {
            List<State> states = mapJsonToStates(json);
            for (State state: states) {
                updateStateTimeSeries(state);
                indianStatesRepository.upsertState(state.getStateName(), state);
            }
        }
    }

    private List<State> mapJsonToStates(String json) throws Exception {
        mapper.findAndRegisterModules();
        try {
            Map<String, Map<String, Map<String, List<Map<String, String>>>>> map = mapper.readValue(json, Map.class);
            if (map.size() > 0) {
                Map<String, Map<String, List<Map<String, String>>>> stateMap = map.get("districtsDaily");
                return stateMap.entrySet().stream().map(stateEntry -> {
                    State state = new State();
                    state.setStateName(stateEntry.getKey());
                    state.setLastUpdateOn(LocalDateTime.now());
                    state.setDistricts(stateEntry.getValue().entrySet().stream().map(districtEntry -> {
                        District district = new District();
                        district.setDistrictName(districtEntry.getKey());
                        district.setDistrictTimeSeries(districtEntry.getValue().stream()
                                .map(e -> mapper.convertValue(e, TimeSeries.class))
                                .collect(Collectors.toList()));
                            return district;
                        }).collect(Collectors.toList()));
                    return state;
                    }).collect(Collectors.toList());
                } else {
                log.error("Error loading states list");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    private void updateStateTimeSeries(State state) {
        for(District district: state.getDistricts()) {
            updateDistrictTimeSeriesDeltaStats(district);
        }

        List<TimeSeries> series = state.getStateTimeSeries();
        if (nonNull(series)) {
            if (series.size() > 0) {
                for(int i = 0; i < series.size(); i++) {
                    TimeSeries today = series.get(i);
                    today.setActive(today.getConfirmed() - (today.getRecovered() + today.getDeceased()));
                    calculateDelta(i, series, today);
                }
            } else {
                // TimeSeries is set for first time
                // StateTimeSeries is based on the total of all districts each day
                state.setStateTimeSeries(createStateTimeSeries(state.getDistricts()));
            }
        } else {
            // TimeSeries is set for first time
            // StateTimeSeries is a total of all districts in one day
            state.setStateTimeSeries(createStateTimeSeries(state.getDistricts()));
        }
        state.setTotal(state.getStateTimeSeries().get(state.getStateTimeSeries().size()-1));
    }


    private List<TimeSeries> createStateTimeSeries(List<District> districts) {
        int i = 0;
        List<TimeSeries> stateSeries = new ArrayList<>();

        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.now();

        Stream<LocalDate> dates = start.datesUntil(end);
        List<LocalDate> dateList = dates.collect(Collectors.toList());

        for(LocalDate date: dateList) {
            TimeSeries today = new TimeSeries();
            Date d = Date.from(date.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC));
            today.setDate(d);
            for (District district: districts) {
                TimeSeries timeSeries = district.getDistrictTimeSeries().stream().filter(series -> series.getDate().equals(d)).findAny()
                        .orElse(null);
                if (nonNull(timeSeries)) {
                    today.setActive(today.getActive() + timeSeries.getActive());
                    today.setConfirmed(today.getConfirmed() + timeSeries.getConfirmed());
                    today.setRecovered(today.getRecovered() + timeSeries.getRecovered());
                    today.setDeceased(today.getDeceased() + timeSeries.getDeceased());
                }
            }
            calculateDelta(i, stateSeries, today);
            stateSeries.add(today);
            i++;
        }
        return stateSeries;
    }


    private void updateDistrictTimeSeriesDeltaStats(District district) {
        List<TimeSeries> series = district.getDistrictTimeSeries();

        for(int i = 0; i < series.size(); i++) {
            TimeSeries today = series.get(i);
            calculateDelta(i, series, today);
        }
        //Total Stats of district
        district.setTotal(series.get(series.size()-1));
    }

    private void calculateDelta(int index, List<TimeSeries> stateSeries, TimeSeries today) {
        Delta delta = new Delta();
        if (index == 0) {
            delta.setRecovered(0);
            delta.setDeceased(0);
            delta.setConfirmed(0);
            delta.setActive(0);
        } else {
            TimeSeries previousDay = stateSeries.get(index-1);
            delta.setRecovered(today.getRecovered() - previousDay.getRecovered());
            delta.setDeceased(today.getDeceased() - previousDay.getDeceased());
            delta.setConfirmed(today.getConfirmed() - previousDay.getConfirmed());
            delta.setActive(today.getActive() - previousDay.getActive());
        }
        today.setDelta(delta);
    }

    @Override
    public void updateIndianZones() throws Exception {
        String json = restClient.getIndianZones().orElse(null);
        if (nonNull(json)) {
            mapZones(json);
        }
    }

    private void mapZones(String json) throws Exception {
        mapper.findAndRegisterModules();
        try {
            Map<String, List<Map<String, String>>> map = mapper.readValue(json, Map.class);
            map.get("zones").forEach(value -> {
                State state = indianStatesRepository.findByStateNameIgnoreCase(value.get("state")).orElse(null);
                if (nonNull(state)) {
                    District district = state.getDistricts().stream().filter(d -> d.getDistrictName().equals(value.get("district")))
                            .findAny().orElse(null);
                    if (nonNull(district)) {
                        district.setZone(value.get("zone"));
                    }
                    indianStatesRepository.upsertState(state.getStateName(), state);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<State> getAllIndianStatesStats() {
        return indianStatesRepository.findAll();
    }

    @Override
    public State getStateStats(String stateName) {
        return indianStatesRepository.findByStateNameIgnoreCase(stateName).orElse(null);
    }

    @Override
    public District getDistrictStats(String stateName, String districtName) {
        State state = indianStatesRepository.findByStateNameIgnoreCase(stateName).orElse(null);
        if (nonNull(state)) {
            return state.getDistricts().stream()
                    .filter(district -> district.getDistrictName().equalsIgnoreCase(districtName))
                    .findAny().orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public List<Resources> getResources(String stateName, String districtName) {
        return resourcesRepository.findByStateAndCityAllIgnoreCase(stateName, districtName);
    }

    @Override
    public List<Resources> getResources(String stateName) {
        return resourcesRepository.findByStateIgnoreCase(stateName);
    }

    @Override
    public List<ChartObject> getChartData(String state, String district) {
        List<TimeSeries> series;
        if (nonNull(district)) {
            series = Objects.requireNonNull(Objects.requireNonNull(indianStatesRepository.findByStateNameIgnoreCase(state).orElse(null))
                    .getDistricts().stream().filter(d -> d.getDistrictName().equalsIgnoreCase(district)).findAny().orElse(null))
                    .getDistrictTimeSeries();
        } else {
            series = Objects.requireNonNull(indianStatesRepository.findByStateNameIgnoreCase(state).orElse(null))
                    .getStateTimeSeries();
        }
        List<ChartObject> list = new ArrayList<>();
        ChartObject confirmedStatsObject = new ChartObject("confirmed");
        ChartObject activeStatsObject = new ChartObject("active");
        ChartObject recoveredStatsObject = new ChartObject("recovered");
        ChartObject deceasedStatsObject = new ChartObject("deceased");

        for (TimeSeries timeSeries: series) {
            LocalDate date = timeSeries.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            FrontendChartData confirm = new FrontendChartData();
            confirm.setDate(date);
            confirm.setCount(timeSeries.getConfirmed());
            confirmedStatsObject.getChartData().add(confirm);

            FrontendChartData active = new FrontendChartData();
            active.setDate(date);
            active.setCount(timeSeries.getActive());
            activeStatsObject.getChartData().add(active);

            FrontendChartData recovered = new FrontendChartData();
            recovered.setDate(date);
            recovered.setCount(timeSeries.getRecovered());
            recoveredStatsObject.getChartData().add(recovered);

            FrontendChartData deceased = new FrontendChartData();
            deceased.setDate(date);
            deceased.setCount(timeSeries.getDeceased());
            deceasedStatsObject.getChartData().add(deceased);
        }
        list.add(confirmedStatsObject);
        list.add(activeStatsObject);
        list.add(recoveredStatsObject);
        list.add(deceasedStatsObject);
        return list;
    }


}
