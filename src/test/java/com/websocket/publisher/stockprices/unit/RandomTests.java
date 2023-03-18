package com.websocket.publisher.stockprices.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class RandomTests {

    @Test
    void testFluxOperators(){
        //Flux Operators
        Flux.range(1, 10)
                .filter(x -> x % 2 == 0)
                .subscribe(System.out::println); // output: 2 4 6 8 10
        Flux.range(1, 3)
                .map(x -> x * 2)
                .subscribe(System.out::println); // output: 2 4 6
        Flux.range(1, 3)
                .flatMap(x -> Flux.just(x, x * 2))
                .subscribe(System.out::println); // output: 1 2 2 4 3 6
        Flux.range(1, 3)
                .doOnNext(x -> System.out.println("Received: " + x))
                .subscribe(System.out::println); // output: Received: 1 \n 1 \n Received: 2 \n 2 \n Received: 3 \n 3
        Flux.range(1, 3)
                .doOnComplete(() -> System.out.println("Completed!"))
                .subscribe(System.out::println); // output: 1 2 3 \n Completed!
        Flux.error(new RuntimeException("Oops!"))
                .doOnError(Throwable::printStackTrace)
                .subscribe(); // output: java.lang.RuntimeException: Oops! ...
        Flux.error(new RuntimeException("Oops!"))
                .onErrorResume(e -> Flux.just("Fallback"))
                .subscribe(System.out::println); // output: Fallback
        Flux.range(1, 3)
                .zipWith(Flux.just("A", "B", "C"))
                .map(t -> t.getT1() + t.getT2())
                .subscribe(System.out::println); // output: 1A 2B 3C
        Flux.just(1, 2, 3)
                .concatWith(Flux.just(4, 5, 6))
                .subscribe(System.out::println); // output: 1 2 3 4 5 6


        //Mono Operators
        Mono.just(5)
                .filter(x -> x > 3)
                .subscribe(System.out::println); // output: 5››
        Mono.just("Hello")
                .map(String::toUpperCase)
                .subscribe(System.out::println); // output: HELLO
        Mono.just(3)
                .flatMap(x -> Mono.just(x * 2))
                .subscribe(System.out::println); // output: 6
        Mono.just("Hello")
                .doOnNext(x -> System.out.println("Received: " + x))
                .subscribe(System.out::println); // output: Received: Hello \n Hello
        Mono.just("Hello")
                .doOnSuccess(x -> System.out.println("Success!"))
                .subscribe(System.out::println); // output: Hello \n Success!
        Mono.error(new RuntimeException("Oops!"))
                .doOnError(Throwable::printStackTrace)
                .subscribe(); // output: java.lang.RuntimeException: Oops! ...
        Mono.error(new RuntimeException("Oops!"))
                .onErrorResume(e -> Mono.just("Fallback"))
                .subscribe(System.out::println); // output: Fallback
        Mono.zip(Mono.just("Hello"), Mono.just("World"))
                .map(t -> t.getT1() + " " + t.getT2())
                .subscribe(System.out::println); // output: Hello World
        Mono.just("Hello")
                .zipWhen(x -> Mono.just(x + " World"))
                .map(t -> t.getT1() + " " + t.getT2())
                .subscribe(System.out::println); // output: Hello World

    }
}
