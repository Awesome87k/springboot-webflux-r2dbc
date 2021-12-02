package myspringboot.reactive.webflux.controller;

import myspringboot.reactive.webflux.dto.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerControllerWebTestClient {

    @Autowired
    private WebTestClient client;

    @Test
    public void getCustomersEvent() {
        Flux<Customer> customerFlux = client.get()
                .uri("/customers/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange() //응답반환
                .expectStatus()
                .isOk()
                .returnResult(Customer.class)
                .getResponseBody()
                .log();

        StepVerifier.create(customerFlux)
                .expectNextCount(10)
                .verifyComplete();

    }
}
