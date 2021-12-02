package myspringboot.reactive.webflux.r2dbc.controller;

import myspringboot.reactive.webflux.r2dbc.domain.Customer;
import myspringboot.reactive.webflux.r2dbc.repository.CustomerRepository;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/r2dbc/customers")
public class R2CustomerController {
    private final CustomerRepository repository;

    /*
        A요청 -> Flux -> Stream
        B요청 -> Flux -> Stream
        Sinks는 Flux.merge로 두 개의 Flux를 합쳐주는 역학을 한다.
     */
    private final Sinks.Many<Customer> sinks;

    public R2CustomerController(CustomerRepository repository) {
        this.repository = repository;
        this.sinks = Sinks.many()
                .multicast()
                .onBackpressureBuffer();
    }

    //data가 모두 subscribe되면 종료된다.
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)       //produces = MediaType.TEXT_EVENT_STREAM_VALUE 동적으로 데이터가 로드되는대로 바로 응답을 넘긴다
    public Flux<Customer> findAllCustomer() {
        return repository.findAll()
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("/{id}")
    public Mono<Customer> findCustomer(
            @PathVariable Long id
    ) {
        return repository.findById(id).log();
    }

    //data가 모두 subscribe되어도 종료되지않고 계속 데이터를 전송한다..
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Customer>> findAllCustomerSSE() {       //ServerSentEvent를 리턴타입으로 선언하면  (produces = MediaType.TEXT_EVENT_STREAM_VALUE) 생략가능
        return  sinks.asFlux()
                .mergeWith(repository.findAll())    //기존의 데이터를 가져온다
                .map(customer -> ServerSentEvent.builder(customer).build()) //추가되는 데이터를 클라이언트쪽으로 밀어준다.
                .doOnCancel(() -> sinks.asFlux().blockLast())   //사용자쪽에서 캔슬시 complete시그널을 내린다. > 처리하지 않을시 동시에 접근중인 다른 사람도 같이 캔슬 될 수 있다... >> 다시확인필요
                .log();
    }

    @PostMapping
    public Mono<Customer> saveCustomer(
            @RequestBody Customer _customer
    ) {
        return repository.save(_customer)
                .doOnNext(cust -> sinks.tryEmitNext(cust))  //정상 등록되었다면 sinks에 emit하여 데이터를 합쳐준다
                .log();
    }
}
