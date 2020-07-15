package com.app.covidtracker.serviceImpl;

import com.app.covidtracker.service.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Component
public class RestClientImpl implements RestClient {

    @Value("${global.stats}")
    private String globalStatsEndpoint;

    @Value("${india.resources}")
    private String resourcesEndpoint;

    @Value("${india.districts.stats}")
    private String indianStatesEndpoint;

    @Value("${india.zones}")
    private String indianZonesEndpoint;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Override
    public Optional<String> getGlobalStats() {
        return executor()
                .operationExecutor(() -> restTemplate.getForObject(globalStatsEndpoint, String.class))
                .errorLog(reason -> log.error("Unable to get data. Reason: {}", reason))
                .execute();
    }

    @Override
    public Optional<String> getIndiaResources() {
        return executor()
                .operationExecutor(() -> restTemplate.getForObject(resourcesEndpoint, String.class))
                .errorLog(reason -> log.error("Unable to get Indian Resources: {}", reason))
                .execute();
    }

    @Override
    public Optional<String> getIndianStatsStats() {
        return executor()
                .operationExecutor(() -> restTemplate.getForObject(indianStatesEndpoint, String.class))
                .errorLog(reason -> log.error("Unable to get Indian States Stats: {}", reason))
                .execute();
    }

    @Override
    public Optional<String> getIndianZones() {
        return executor()
                .operationExecutor(() -> restTemplate.getForObject(indianZonesEndpoint, String.class))
                .errorLog(reason -> log.error("Unable to get Indian Zones: {}", reason))
                .execute();
    }

    private GlobalStatsOperationExecutor executor() {
        return new GlobalStatsOperationExecutor();
    }

    private static class GlobalStatsOperationExecutor {
        private GlobalStatsApiOperation apiOperation;
        private Runnable missingBodyLog = () -> log.error("Response Body is missing. Something has changed or an error has occured");
        private Runnable successLog = () -> log.info("Successfully loaded data");
        private Consumer<String> errorLog = reason -> log.error("Error fetching data from endpoint. Reason: {}", reason);

        public GlobalStatsOperationExecutor operationExecutor(final GlobalStatsApiOperation apiOperation) {
            this.apiOperation = apiOperation;
            return this;
        }

        public GlobalStatsOperationExecutor errorLog(final Consumer<String> errorLog) {
            this.errorLog = errorLog;
            return this;
        }

        public Optional<String> execute() {
            try {
                final String jsonString = apiOperation.call();
                if (isNull(jsonString)) {
                    missingBodyLog.run();
                } else {
                    successLog.run();
                }
                return Optional.of(jsonString);
            } catch (HttpStatusCodeException e) {
                final String errorResponse = e.getResponseBodyAsString();
                final String reason = isNotBlank(errorResponse) ? errorResponse : e.getStatusCode().toString();
                errorLog.accept(reason);
                return Optional.empty();
            }
        }
    }

    @FunctionalInterface
    private interface GlobalStatsApiOperation {
        String call() throws HttpStatusCodeException;
    }
}
