package com.app.covidtracker.domain.india;

import com.app.covidtracker.domain.TimeSeries;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class District {
    @Id
    private String id;
    @Indexed
    private String districtName;
    private List<TimeSeries> districtTimeSeries;
    private LocalDateTime lastModifiedOn;
    // total stats of district
    private TimeSeries total;
    private String zone;
}
