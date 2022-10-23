package com.app.covidtracker.domain.frontend;

import com.app.covidtracker.domain.TimeSeries;
import com.app.covidtracker.domain.india.District;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class FrontendState {
    private String stateName;
    private TimeSeries total;
    private Date lastUpdatedDate;
    private List<District> districts;
}
