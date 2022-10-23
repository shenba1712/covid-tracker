package com.app.covidtracker.domain.india;

import com.app.covidtracker.domain.Delta;
import com.app.covidtracker.domain.TimeSeries;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "indianStates")
public class State {
    @Id
    private String id;
    @Indexed
    private String stateName;
    private List<District> districts;
    private List<TimeSeries> stateTimeSeries;
    // total stats of the state
    private TimeSeries total;
    @LastModifiedDate
    private LocalDateTime lastUpdateOn;
}