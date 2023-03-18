package com.websocket.publisher.stockprices.application.api.inbound;

import com.websocket.publisher.stockprices.application.usecases.StockPriceUseCase;
import com.websocket.publisher.stockprices.domain.eapi.StockPriceResponse;
import com.websocket.publisher.stockprices.domain.stockprices.PriceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
public class StockPricesController {
    private final StockPriceUseCase stockPriceUseCase;
    private static final String STOCK_PRICES_URL = "/v1/stock/price";

    @PostMapping(value = STOCK_PRICES_URL,
    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<StockPriceResponse> getStockPrices(@RequestBody PriceRequest request){
        return stockPriceUseCase.run(request);
    }

}
