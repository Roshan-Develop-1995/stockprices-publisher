package com.websocket.publisher.stockprices.domain.stockprices;

import com.websocket.publisher.stockprices.domain.eapi.StockPriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockPriceService implements PriceService<StockPriceResponse, PriceRequest>{

    private final StockPriceExternalSource externalSource;

    @Override
    public Mono<StockPriceResponse> getPriceFromService(PriceRequest request) {
        Mono<PriceResponse> priceResponse = externalSource.getStockPriceFromClient(request);

        return priceResponse.flatMap(response ->
                Mono.just(
                        StockPriceResponse.builder()
                                .stockCode(request.getStockCode())
                                .stockPrice(response.getPrice())
                                .build()
                        )
                )
                .doOnSuccess(response -> log.info("[Stock Price Service] Response: "+response));
    }
}
