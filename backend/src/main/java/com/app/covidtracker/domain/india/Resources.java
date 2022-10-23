package com.app.covidtracker.domain.india;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "indianResources")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resources {
    @Id
    private String id;
    @Indexed
    private String category;
    @Indexed
    private String city;
    private String contact;
    @JsonAlias(value = "descriptionandorserviceprovided")
    private String description;
    @JsonAlias(value = "nameoftheorganisation")
    private String organisation;
    @JsonAlias(value = "phonenumber")
    private String phoneNumber;
    @Indexed
    private String state;
    private Double lat;
    private Double lng;
}
