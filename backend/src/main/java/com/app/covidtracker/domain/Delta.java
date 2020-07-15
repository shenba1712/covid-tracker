package com.app.covidtracker.domain;

import lombok.Data;

// Daily increase or decrease in value
@Data
public class Delta {
    private long confirmed;
    private long deceased;
    private long recovered;
    private long active;
}
