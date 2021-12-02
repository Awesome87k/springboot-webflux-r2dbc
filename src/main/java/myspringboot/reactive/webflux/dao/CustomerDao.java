package myspringboot.reactive.webflux.dao;

import myspringboot.reactive.webflux.dto.Customer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CustomerDao {

    public List<Customer> getCustomers() {
        return IntStream.rangeClosed(1, 10)
                .peek(CustomerDao::test)    //  이렇게 쓸수있음.peek(i -> test(i))
                .peek(i -> System.out.println("processing count : " + i))   //라인별로 로그찍을때..
                .mapToObj(i -> new Customer(i, "Customer" + i))
                .collect(Collectors.toList());
    }

    private static void test(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Flux<Customer> getCustomerStream() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println("processing count : " + i))   //진행과정의추가 동작
                .map(i -> new Customer(i, "Customer" + i));
    }

    public Flux<Customer> getCustomerNotDelay() {
        return Flux.range(1, 10)
                .doOnNext(i -> System.out.println("processing count : " + i))   //진행과정의추가 동작
                .map(i -> new Customer(i, "Customer" + i));
    }

}