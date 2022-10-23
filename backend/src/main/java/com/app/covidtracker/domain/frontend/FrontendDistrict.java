package com.app.covidtracker.domain.frontend;

import com.app.covidtracker.domain.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class FrontendDistrict {
    private String districtName;
    private TimeSeries total;
    private Date lastUpdatedDate;
    private String zone;
}
