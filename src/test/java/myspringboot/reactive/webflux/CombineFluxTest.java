package myspringboot.reactive.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class CombineFluxTest {

    @Test
    public void combineUsingMerge() {
        Flux<String> lastNames = Flux.just("김", "박", "홍").delayElements(Duration.ofSeconds(1));
        Flux<String> firstNames = Flux.just("영희", "둘리", "길동").delayElements(Duration.ofSeconds(1));

//        Flux<String> names = Flux.merge(lastNames, firstNames).log();   //순서를 보장하지 않는다
        Flux<String> names = Flux.concat(lastNames, firstNames).log();  //순서를 보장한다

//        names.subscribe(System.out::println, System.out::println);

        StepVerifier.create(names)
                .expectSubscription()
                .expectNext("김", "박", "홍", "영희", "둘리", "길동")
                .verifyComplete();
    }

    @Test
    public void combineUsingZipDelay() {
        Flux<String> lastNames = Flux.just("김", "박", "홍").delayElements(Duration.ofSeconds(1));
        Flux<String> firstNames = Flux.just("영희", "둘리", "길동").delayElements(Duration.ofSeconds(1));

        Flux<String> names = Flux.zip(
                lastNames
                , firstNames
                , (n1, n2) -> n1.concat(" ").concat(n2)
        ).log();  //순서를 보장한다

//        names.subscribe(System.out::println, System.out::println);

        StepVerifier.create(names)
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }
}
