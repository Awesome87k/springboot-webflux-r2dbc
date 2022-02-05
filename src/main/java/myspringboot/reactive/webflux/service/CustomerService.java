package myspringboot.reactive.webflux.service;

import myspringboot.reactive.webflux.dao.CustomerDao;
import myspringboot.reactive.webflux.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerDao customerDao;

    public List<Customer> loadAllCustomers() {

        long start = System.currentTimeMillis();
        List<Customer> customerList = customerDao.getCustomers();
        long end = System.currentTimeMillis();
        System.out.println("Total Execution Time = " + (end - start) + "ms");
        System.out.println("전체 실행시간 = " + (end - start) + "ms");

        return customerList;
    }

    public Flux<Customer> loadAllCusotomerStream() {
        long start = System.currentTimeMillis();
        return customerDao.getCustomerStream()
                .doFinally(val -> System.out.println("Total Execution Time = " + (System.currentTimeMillis()- start) + "ms")); //동작이 최종적으로 완료될때 동작함
    }

}