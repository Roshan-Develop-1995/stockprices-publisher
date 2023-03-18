package com.websocket.publisher.stockprices.application.api.outbound.config;

import com.websocket.publisher.stockprices.domain.exception.RetryException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.util.Objects.isNull;

@Getter
public class ClientFilter {
    private final AtomicReference<String> errorDetail = new AtomicReference<>();
    private final Integer maxRetryAttempts;
    private final Duration retryInterval;
    private final Consumer<? super Throwable> handleRetryError = error ->{
        if(error.getClass().getCanonicalName().equals("reactor.core.RetryExhaustedException")){
            throw new RetryException(errorDetail.get());
        }
    };

    public ClientFilter(Integer maxRetryAttempts, Duration retryInterval) {
        this.maxRetryAttempts = maxRetryAttempts;
        this.retryInterval = retryInterval;
    }

    public @NonNull Mono<ClientResponse> retryFilter(ClientRequest request, ExchangeFunction next){
        return ExchangeFilterFunction
                .ofResponseProcessor(response -> checkForErrorRetry(response, request))
                .apply(next)
                .exchange(request)
                .retryWhen(Retry.fixedDelay(maxRetryAttempts, retryInterval)
                        .filter(this::isErrorToRetry))
                .doOnError(this.handleRetryError);
    }

    private boolean isErrorToRetry(Throwable error) {
        errorDetail.set(error.getLocalizedMessage());
        return (
                (error instanceof WebClientRequestException
                && error.getCause() instanceof ReadTimeoutException)
                || error instanceof RetryException
        );
    }

    private Mono<ClientResponse> checkForErrorRetry(ClientResponse response, ClientRequest request) {
        errorDetail.set("");
        int statusCode = response.rawStatusCode();
        if(HttpStatus.Series.resolve(statusCode) != HttpStatus.Series.SERVER_ERROR){
            return Mono.just(response);
        }
        String reasonPhrase =
                isNull(HttpStatus.resolve(statusCode))? "": response.statusCode().getReasonPhrase();

        return response
                .bodyToMono(String.class)
                .flatMap(body -> Mono.error(
                        new RetryException(statusCode + reasonPhrase + " from " + request.method()
                        + " " + request.url() + " with error ["+ body +"]")
                ));
    }
}
