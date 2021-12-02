package myspringboot.reactive.webflux.r2dbc.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
public class Customer {

    @Id
    private Long id;

    private final String firstName;
    private final String lastName;

}