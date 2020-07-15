package com.app.covidtracker.domain.india;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public class Tests {
    private float individualsTestedPerConfirmedCases;
    private float positiveCasesFromSamplesReported;
    private float samplesReportedToday;
    private String source;
    private float testPositivityRate;
    private float testsByPrivateLabs;
    private float testsPerConfirmedCases;
    private float totalIndividuals;
    private float totalPositive;
    private float totalSamples;
    @LastModifiedDate
    private LocalDateTime lastModifiedOn;
}
