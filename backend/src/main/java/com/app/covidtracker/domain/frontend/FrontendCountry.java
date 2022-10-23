package com.app.covidtracker.domain.frontend;

import com.app.covidtracker.domain.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@AllArgsConstructor
@Data
public class FrontendCountry {
    private String countryName;
    private TimeSeries total;
    private Date lastUpdatedDate;
}
