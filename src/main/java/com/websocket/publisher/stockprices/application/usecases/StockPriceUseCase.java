package com.websocket.publisher.stockprices.application.usecases;

import com.websocket.publisher.stockprices.domain.eapi.StockPriceResponse;
import com.websocket.publisher.stockprices.domain.stockprices.PriceRequest;
import com.websocket.publisher.stockprices.domain.stockprices.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockPriceUseCase implements UseCase<StockPriceResponse, PriceRequest>{

    private final StockPriceService stockPriceService;

    @Override
    public Mono<StockPriceResponse> run(PriceRequest request) {
        return stockPriceService.getPriceFromService(request);
    }
}
