package com.app.covidtracker.domain.frontend;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChartObject {
    String statName;
    List<FrontendChartData> chartData = new ArrayList<>();

    public ChartObject(String statName) {
        this.statName = statName;
    }
}
