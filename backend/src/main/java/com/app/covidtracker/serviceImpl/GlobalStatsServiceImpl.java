package com.app.covidtracker.serviceImpl;

import com.app.covidtracker.domain.Country;
import com.app.covidtracker.domain.Delta;
import com.app.covidtracker.domain.GlobalTimeSeries;
import com.app.covidtracker.domain.TimeSeries;
import com.app.covidtracker.domain.frontend.ChartObject;
import com.app.covidtracker.domain.frontend.FrontendChartData;
import com.app.covidtracker.repository.CountryRepository;
import com.app.covidtracker.repository.GlobalTimeSeriesRepository;
import com.app.covidtracker.service.GlobalStatsService;
import com.app.covidtracker.service.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class GlobalStatsServiceImpl implements GlobalStatsService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private GlobalTimeSeriesRepository globalTimeSeriesRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void updateAllCountryStats() {
        String json = restClient.getGlobalStats().orElse(null);
        if (nonNull(json)) {
            List<Country> countries = mapJsonToCountry(json);
            for (Country country: countries) {
                updateCountryTimeSeriesDeltaStats(country);
                countryRepository.upsertCountry(country.getCountryName(), country);
            }
        }
    }

    private List<Country> mapJsonToCountry(String json) {
        mapper.findAndRegisterModules();
        try {
            Map<String, List<Map<String, String>>> map = mapper.readValue(json, Map.class);
            return map.entrySet().stream().map(entry -> {
                Country country = new Country();
                country.setCountryName(entry.getKey());
                country.setTimeSeries(entry.getValue().stream()
                        .map(e -> mapper.convertValue(e, TimeSeries.class))
                        .collect(Collectors.toList()));
                return country;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void updateCountryTimeSeriesDeltaStats(Country country) {
        List<TimeSeries> series = country.getTimeSeries();
        for(int i = 0; i < series.size(); i++) {
            TimeSeries today = series.get(i);
            today.setActive(today.getConfirmed() - (today.getRecovered() + today.getDeceased()));
            Delta delta = new Delta();
            if (i == 0) {
                delta.setRecovered(0);
                delta.setDeceased(0);
                delta.setConfirmed(0);
                delta.setActive(0);
            } else {
                TimeSeries previousDay = series.get(i-1);
                delta.setRecovered(today.getRecovered() - previousDay.getRecovered());
                delta.setDeceased(today.getDeceased() - previousDay.getDeceased());
                delta.setConfirmed(today.getConfirmed() - previousDay.getConfirmed());
                delta.setActive(today.getActive() - previousDay.getActive());
            }
            today.setDelta(delta);
        }
        //Total Stats of Country
        country.setTotal(series.get(series.size()-1));
    }

    @Override
    public List<Country> getAllCountryStats() {
        return countryRepository.findAll();
    }

    @Override
    public void updateGlobalStats() {
        if (globalTimeSeriesRepository.count() > 0) {
            // add 1 to get the date from which the value needs to be updated.
            LocalDate start = globalTimeSeriesRepository.findFirstByOrderByDateDesc().getDate().plusDays(2);
            LocalDate end = LocalDate.now();

            List<LocalDate> dateList = start.datesUntil(end).collect(Collectors.toList());

            for(LocalDate date: dateList) {
                GlobalTimeSeries globalTimeSeries = new GlobalTimeSeries();
                globalTimeSeries.setDate(date);

                TimeSeries totalTimeSeries = calculateTimeSeries(date);
                totalTimeSeries.setDelta(calculateDelta(date, totalTimeSeries));

                globalTimeSeries.setTotal(totalTimeSeries);
                globalTimeSeries.setLastModifiedOn(LocalDateTime.now());
                globalTimeSeriesRepository.save(globalTimeSeries);
            }
        } else {
            // The global timeSeries is being created the first time. So need to calculate the the series from the beginning.
            LocalDate start = LocalDate.of(2020, 1, 1);
            LocalDate end = LocalDate.now();
            Stream<LocalDate> dates = start.datesUntil(end);
            List<LocalDate> dateList = dates.collect(Collectors.toList());

            for(LocalDate date: dateList) {

                GlobalTimeSeries globalTimeSeries = new GlobalTimeSeries();
                globalTimeSeries.setDate(date);

                TimeSeries timeSeries = calculateTimeSeries(date);
                Delta delta = new Delta();

                // will have nothing to compare with before
                if (date.equals(start)) {
                    delta.setActive(0);
                    delta.setDeceased(0);
                    delta.setConfirmed(0);
                    delta.setRecovered(0);
                } else {
                    delta = calculateDelta(date, timeSeries);
                }
                timeSeries.setDelta(delta);
                globalTimeSeries.setTotal(timeSeries);
                globalTimeSeries.setLastModifiedOn(LocalDateTime.now());
                globalTimeSeriesRepository.save(globalTimeSeries);
            }
        }
    }

    private TimeSeries calculateTimeSeries(LocalDate date) {
        TimeSeries timeSeries = new TimeSeries();
        Date d = Date.from(date.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC));
        timeSeries.setDate(d);
        for (Country country: getAllCountryStats()) {
            TimeSeries todaySeries = country.getTimeSeries().stream().filter(series -> series.getDate().equals(d)).findAny()
                    .orElse(null);
            if (nonNull(todaySeries)) {
                timeSeries.setActive(timeSeries.getActive() + todaySeries.getActive());
                timeSeries.setConfirmed(timeSeries.getConfirmed() + todaySeries.getConfirmed());
                timeSeries.setRecovered(timeSeries.getRecovered() + todaySeries.getRecovered());
                timeSeries.setDeceased(timeSeries.getDeceased() + todaySeries.getDeceased());
            }
        }
        return timeSeries;
    }

    private Delta calculateDelta(LocalDate date, TimeSeries timeSeries) {
        Delta delta = new Delta();
        LocalDate yesterday = date.minusDays(1);
        GlobalTimeSeries ydayGlobalSeries = globalTimeSeriesRepository.findByDate(yesterday);
        if (nonNull(ydayGlobalSeries)) {
            delta.setRecovered(timeSeries.getRecovered() - ydayGlobalSeries.getTotal().getRecovered());
            delta.setDeceased(timeSeries.getDeceased() - ydayGlobalSeries.getTotal().getDeceased());
            delta.setConfirmed(timeSeries.getConfirmed() - ydayGlobalSeries.getTotal().getConfirmed());
            delta.setActive(timeSeries.getActive() - ydayGlobalSeries.getTotal().getActive());
        }
        return delta;
    }

    @Override
    public GlobalTimeSeries getLatestGlobalStats() {
        return globalTimeSeriesRepository.count() != 0 ? globalTimeSeriesRepository.findFirstByOrderByDateDesc() : null;
    }

    @Override
    public Country getCountryStats(String countryName) {
        return countryRepository.findByCountryNameIgnoreCase(countryName).orElse(null);
    }

    @Override
    public List<ChartObject> getGlobalChartData() {
        List<GlobalTimeSeries> globalSeries = globalTimeSeriesRepository.findAll();
        List<ChartObject> list = new ArrayList<>();
        ChartObject confirmedStatsObject = new ChartObject("confirmed");
        ChartObject activeStatsObject = new ChartObject("active");
        ChartObject recoveredStatsObject = new ChartObject("recovered");
        ChartObject deceasedStatsObject = new ChartObject("deceased");

        for (GlobalTimeSeries timeSeries: globalSeries) {
            TimeSeries series = timeSeries.getTotal();

            FrontendChartData confirm = new FrontendChartData();
            confirm.setDate(timeSeries.getDate());
            confirm.setCount(series.getConfirmed());
            confirmedStatsObject.getChartData().add(confirm);

            FrontendChartData active = new FrontendChartData();
            active.setDate(timeSeries.getDate());
            active.setCount(series.getActive());
            activeStatsObject.getChartData().add(active);

            FrontendChartData recovered = new FrontendChartData();
            recovered.setDate(timeSeries.getDate());
            recovered.setCount(series.getRecovered());
            recoveredStatsObject.getChartData().add(recovered);

            FrontendChartData deceased = new FrontendChartData();
            deceased.setDate(timeSeries.getDate());
            deceased.setCount(series.getDeceased());
            deceasedStatsObject.getChartData().add(deceased);
        }
        list.add(confirmedStatsObject);
        list.add(activeStatsObject);
        list.add(recoveredStatsObject);
        list.add(deceasedStatsObject);
        return list;
    }

    @Override
    public List<ChartObject> getCountryChartData(String country) {
        List<TimeSeries> countrySeries = Objects.requireNonNull(countryRepository.findByCountryNameIgnoreCase(country).
                orElse(null)).getTimeSeries();
        List<ChartObject> list = new ArrayList<>();
        ChartObject confirmedStatsObject = new ChartObject("confirmed");
        ChartObject activeStatsObject = new ChartObject("active");
        ChartObject recoveredStatsObject = new ChartObject("recovered");
        ChartObject deceasedStatsObject = new ChartObject("deceased");

        for (TimeSeries series: countrySeries) {
            LocalDate date = series.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            FrontendChartData confirm = new FrontendChartData();
            confirm.setDate(date);
            confirm.setCount(series.getConfirmed());
            confirmedStatsObject.getChartData().add(confirm);

            FrontendChartData active = new FrontendChartData();
            active.setDate(date);
            active.setCount(series.getActive());
            activeStatsObject.getChartData().add(active);

            FrontendChartData recovered = new FrontendChartData();
            recovered.setDate(date);
            recovered.setCount(series.getRecovered());
            recoveredStatsObject.getChartData().add(recovered);

            FrontendChartData deceased = new FrontendChartData();
            deceased.setDate(date);
            deceased.setCount(series.getDeceased());
            deceasedStatsObject.getChartData().add(deceased);
        }
        list.add(confirmedStatsObject);
        list.add(activeStatsObject);
        list.add(recoveredStatsObject);
        list.add(deceasedStatsObject);
        return list;
    }
}
