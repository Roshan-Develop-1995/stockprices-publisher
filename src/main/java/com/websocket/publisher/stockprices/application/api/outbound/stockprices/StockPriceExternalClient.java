package com.websocket.publisher.stockprices.application.api.outbound.stockprices;

import com.websocket.publisher.stockprices.application.api.outbound.ExternalClient;
import com.websocket.publisher.stockprices.domain.stockprices.PriceRequest;
import com.websocket.publisher.stockprices.domain.stockprices.PriceResponse;
import com.websocket.publisher.stockprices.domain.stockprices.StockPriceExternalSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StockPriceExternalClient extends ExternalClient implements StockPriceExternalSource {

    @Value("${external-client.stock-prices.path}")
    private String stockPricesPath;
    private static final String CLIENT_NAME = "StockPrices Client";

    protected StockPriceExternalClient(
            @Value("${external-client.stock-prices.domain}") String domainUrl,
            @Autowired WebClient.Builder clientBuilder
    ) {
        super(clientBuilder.baseUrl(domainUrl).build(), CLIENT_NAME);
    }

    @Override
    public Mono<PriceResponse> getStockPriceFromClient(PriceRequest priceRequest) {
        log.info("Request: " + priceRequest);
        long start = System.currentTimeMillis();
        Mono<PriceResponse> apiResponse = this.webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(stockPricesPath).build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(priceRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handle4xxErrorForExternalClient)
                .bodyToMono(PriceResponse.class);

        return getMonoAfterErrorHandler(apiResponse)
                .doOnNext(response ->{
                    long end = System.currentTimeMillis();
                    long responseTime = end - start;
                    log.info("Response received: "+ response);
                    log.info("Time consumed: " + responseTime);
                });
    }
}
