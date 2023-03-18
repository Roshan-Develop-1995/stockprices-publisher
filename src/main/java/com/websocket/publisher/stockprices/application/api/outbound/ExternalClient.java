package com.websocket.publisher.stockprices.application.api.outbound;

import com.websocket.publisher.stockprices.domain.exception.ExternalClientException;
import com.websocket.publisher.stockprices.domain.exception.InternalServerException;
import com.websocket.publisher.stockprices.domain.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class ExternalClient {

    protected final WebClient webClient;
    protected final String clientName;


    protected ExternalClient(WebClient webClient, String clientName) {
        this.webClient = webClient;
        this.clientName = clientName;
    }

    public <T> Mono<T> handle4xxErrorForExternalClient(ClientResponse clientResponse){
        if(HttpStatus.UNAUTHORIZED.value() == clientResponse.rawStatusCode()){
            return clientResponse.bodyToMono(String.class)
                    .flatMap(
                            error -> Mono.error(new UnAuthorizedException("unauthorized "+ error))
                    );
        }
        else{
            return clientResponse.bodyToMono(String.class)
                    .flatMap(
                            error -> Mono.error(
                                    new InternalServerException("["+this.clientName+"] Exception from external api, " +
                                            "satus: "+ clientResponse.rawStatusCode() + error)
                            )
                    );
        }
    }

    public <T> Mono<T> getMonoAfterErrorHandler(Mono<T> clientImplementation){
        return clientImplementation
                .doOnError(DecodingException.class, error -> {
                    log.error("Error encountered ", error);
                    throw new InternalServerException("["+this.clientName+"] Invalid response body, error: " + error);
                })
                .doOnError(
                        throwable -> !(throwable instanceof ExternalClientException),
                        error ->{
                            log.error("Error encountered ", error);
                            throw new InternalServerException("["+this.clientName+"] Error encountered: "+error);
                        }
                );
    }
}
