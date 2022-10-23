package com.app.covidtracker.domain.frontend;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FrontendChartData {
    LocalDate date;
    Long count;
}
