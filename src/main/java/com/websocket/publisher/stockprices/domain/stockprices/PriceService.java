package com.websocket.publisher.stockprices.domain.stockprices;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface PriceService<T,V> {
    Mono<T> getPriceFromService(V request);
}
