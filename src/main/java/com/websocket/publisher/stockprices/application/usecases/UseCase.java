package com.websocket.publisher.stockprices.application.usecases;

import reactor.core.publisher.Mono;

public interface UseCase <T, V>{
    Mono<T> run(V request);
}
