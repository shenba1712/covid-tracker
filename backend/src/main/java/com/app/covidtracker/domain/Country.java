package com.app.covidtracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "country")
public class Country implements Serializable {
    @Id
    private String id;
    @NotNull
    @Indexed(unique = true)
    private String countryName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<TimeSeries> timeSeries;
    private LocalDateTime lastModifiedOn;
    // total stats of the country
    private TimeSeries total;
}