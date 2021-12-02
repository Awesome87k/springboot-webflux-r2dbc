package myspringboot.reactive.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Supplier;

public class MonoTest {

    @Test
    public void justMono() {
        Mono<String> welcome_to_webflux = Mono.just("Welcome to Webflux")
                .map(msg -> msg.concat(".com"))
                .log();
//        welcome_to_webflux.subscribe(System.out::println);

        //test
        StepVerifier.create(welcome_to_webflux)
                .expectNext("Welcome to Webflux.com")
                .verifyComplete();
    }

    @Test
    public void errorMono() {
        Mono<String> message = Mono.error(new RuntimeException("Check Erorr Mono"));
        
        //백프레셔
        message.subscribe(
                value -> {
                    System.out.println("value : "+ value);
                }
                , err -> {
                    err.printStackTrace();
                }
                , () -> {
                    System.out.println("Execution Completed");
                }
        );

        StepVerifier.create(message)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fromSupplier() {
//        Supplier<String> stringSupplier = () -> "Sample Message";
        Mono<String> mono = Mono.fromSupplier(() -> "Sample Message").log();

        mono.subscribe(System.out::println);

        StepVerifier.create(mono)
                .expectNext("Sample Message")
                .verifyComplete();
    }
}
