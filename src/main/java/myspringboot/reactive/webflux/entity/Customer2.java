package myspringboot.reactive.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Customer2 {
    private int id;
    private String name;
    private String email;
    private List<String> phoneNumbers;

    public Customer2(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
