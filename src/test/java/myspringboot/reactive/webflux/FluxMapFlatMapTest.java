package myspringboot.reactive.webflux;

import myspringboot.reactive.webflux.entity.Customer2;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxMapFlatMapTest {
    List<Customer2> customers = List.of(
            new Customer2("gildong", "gildong@gmail.com")
            , new Customer2("dooly", "dooli@gmail.com")
    );

    @Test
    public void transformUsingMap() {
        Flux<Customer2> customerFlux = Flux.fromIterable(customers)
                .map(customer -> new Customer2(customer.getName().toUpperCase(), customer.getEmail().toUpperCase()))
                .log();

//        customerFlux.subscribe(System.out::println);

        //test
        StepVerifier.create(customerFlux)
                .expectNext(new Customer2("GILDONG", "GILDONG@GMAIL.COM"))
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMap() {
        Flux<Customer2> customerFlux = Flux.fromIterable(customers)
                .flatMap(customer -> Mono.just(new Customer2(customer.getName().toUpperCase(), customer.getEmail().toUpperCase())))
                .log();

//        customerFlux.subscribe(System.out::println);

        //test
        StepVerifier.create(customerFlux)
                .expectNext(new Customer2("GILDONG", "GILDONG@GMAIL.COM"))
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void flatMapZipWith() {
        List<String> stringList = Arrays.asList("Olivia",
                "Emma",
                "Ava",
                "Charlotte",
                "Sophia",
                "Amelia",
                "Isabella",
                "Mia",
                "Evelyn");

        Flux<Integer> range = Flux.range(1, Integer.MAX_VALUE);

        Flux.fromIterable(stringList)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .sort()
                .distinct()
                .zipWith(range, (word, line) -> line + ":" + word)
                .subscribe(System.out::println);
    }

}
