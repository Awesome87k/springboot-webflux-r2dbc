package myspringboot.reactive.webflux.controller;

import myspringboot.reactive.webflux.dto.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RestController
public class CustomControllerWebClientTest {
    
    private static WebClient webClient;

    @BeforeAll
    public static void setWebClient() {
        webClient = WebClient.create("http://localhost:8080");

    }

    //http://localhost:8080/customers/stream 테스트진행
    @Test
    public void getAllCustomersEvent() {
        Flux<Customer> customerFlux = webClient.get()
                .uri("/customers/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Customer.class).log();

        StepVerifier.create(customerFlux)
                .expectNextCount(10)
                .verifyComplete();

    }

}
