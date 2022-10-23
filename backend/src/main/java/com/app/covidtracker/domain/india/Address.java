package com.app.covidtracker.domain.india;

import lombok.Data;

@Data
public class Address {
    private String address;
    private double lat;
    private double lng;
}
