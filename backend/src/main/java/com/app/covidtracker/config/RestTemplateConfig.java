package com.app.covidtracker.config;

import com.app.covidtracker.service.RestClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackageClasses = {RestClient.class})
public class RestTemplateConfig {

   @Bean
   @Qualifier("restTemplate")
    public RestTemplate restTemplate(final RestTemplateBuilder builder, HttpComponentsClientHttpRequestFactory factory) {
       return builder.requestFactory(() -> factory).build();
   }

   // To prevent Connection Reset socket exceptions.
   // This happens where there is a period of inactivity and subsequent calls are successful.
   // This could be because of stale connections.
   // The following configuration aims to create a new connection for every call to the API.
   @Bean(name = "factory")
    public HttpComponentsClientHttpRequestFactory factory() {
       RequestConfig requestConfig = RequestConfig.custom()
               .setConnectTimeout(5000)
               .setConnectionRequestTimeout(5000)
               .setSocketTimeout(5000)
               .build();
       CloseableHttpClient httpClient = HttpClients.custom()
               .setDefaultRequestConfig(requestConfig)
               .setConnectionManager(new BasicHttpClientConnectionManager())
               .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
               .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
