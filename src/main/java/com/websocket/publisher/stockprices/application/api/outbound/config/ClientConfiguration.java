package com.websocket.publisher.stockprices.application.api.outbound.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static java.util.Objects.isNull;
import static org.springframework.web.reactive.function.client.WebClient.builder;

@Configuration
public class ClientConfiguration {
    private final Duration timeout;
    private ClientHttpConnector timeoutConnector;
    private final ClientFilter clientFilter;

    @Value("${memory-buffer-size}")
    int bufferSize;


    public ClientConfiguration(
            @Value("${external-client.properties.client-timeout}") Duration timeout,
            @Value("${external-client.properties.max-retry.attempt}") Integer maxRetryAttempts,
            @Value("${external-client.properties.max-retry.interval}") Duration maxRetryInterval
    ) {
        this.timeout = timeout;
        this.clientFilter = new ClientFilter(maxRetryAttempts, maxRetryInterval);
    }

    @Primary
    @Bean
    public WebClient.Builder clientBuilder(){
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(bufferSize)).build();
        return builder()
                .exchangeStrategies(strategies)
                .clientConnector(getTimeoutConnector())
                .filter(clientFilter::retryFilter);
    }

    private ClientHttpConnector getTimeoutConnector() {
        if(isNull(timeoutConnector)){
            timeoutConnector = new ReactorClientHttpConnector(HttpClient.create().responseTimeout(timeout));
        }
        return timeoutConnector;
    }
}
