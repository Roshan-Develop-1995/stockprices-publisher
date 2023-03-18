package com.websocket.publisher.stockprices.domain.stockprices;

import com.websocket.publisher.stockprices.application.ExternalSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface StockPriceExternalSource extends ExternalSource {
    Mono<PriceResponse> getStockPriceFromClient(
            PriceRequest priceRequest
    );
}
