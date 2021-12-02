package myspringboot.reactive.webflux.r2dbc.repository;

import myspringboot.reactive.webflux.r2dbc.domain.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

    @Query("SELECT * FROM customer WHERE last_name = :_lastName")
    Flux<Customer> findByLastName(String _lastName);
}
