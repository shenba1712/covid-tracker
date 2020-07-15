package com.app.covidtracker.domain.frontend;

import com.app.covidtracker.domain.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FrontendState {
    private String stateName;
    private TimeSeries total;
}
