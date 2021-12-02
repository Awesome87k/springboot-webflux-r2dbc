package myspringboot.reactive.webflux.handler;

import myspringboot.reactive.webflux.dao.CustomerDao;
import myspringboot.reactive.webflux.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * MVC Service에 해당
 */
@Component
public class CustomerHandler {
    @Autowired
    private CustomerDao dao;

    //GET으로 검색
    public Mono<ServerResponse> loadCustomerStream(ServerRequest _req) {
        Flux<Customer> customerStream = dao.getCustomerStream();

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(customerStream, Customer.class);
    }

    //GET으로 특정검색
    public Mono<ServerResponse> findCustomer(ServerRequest _req) {
        Integer cutomerId = Integer.valueOf(_req.pathVariable("id"));      //사용자가 요청한 uri 마지막 {id}
        Mono<Customer> customerNotDelay = dao.getCustomerNotDelay()
                .filter(customer -> customer.getId() == cutomerId)
                .next();    //위의 필터링된 결과를 Mono에 결과를 담아주는기능 next()

        return ServerResponse
                .ok()
                .body(customerNotDelay, Customer.class);
    }

    //POST로 저장하는 방법
    public Mono<ServerResponse> saveCustomer(ServerRequest _req) {
        Mono<Customer> customerMono = _req.bodyToMono(Customer.class);
        Mono<String> stringMono = customerMono.map(dto -> dto.getId() + ":" + dto.getName());

        return ServerResponse
                .ok()
                .body(stringMono, String.class);
    }
}
