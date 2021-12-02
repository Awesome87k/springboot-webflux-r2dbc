package myspringboot.reactive.webflux;

import myspringboot.reactive.webflux.entity.Customer2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaStreamTest {

    public static final java.util.stream.Collector<Object, ?, List<Object>> OBJECT_LIST_COLLECTOR = Collectors.toList();

    @Test
    public void lambda() {
        //Functional Interface 함수형 인터페이스, abstract method가 1개만 있는 인터페이스
        //익명의 이너클래스
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(">>>LambdaStreamTest.run1");
            }
        });
        thread.start();

        Thread thread1 = new Thread(() -> System.out.println(">>>LambdaStreamTest.run2"));
        thread1.start();
    }

    /**
     *  Consumer - void accept(T t)
     *  Predicate - boolean test(T t)
     *  Supplier - T get()
     *  Function - R apply(T t)
     *  Operator - T apply(T t, T u)
     *  Method Reference : Lambda excepssion을 좀더 simple하게 작성하는 방법
     */

    @Test
    public void interable() {
        List<String> abc = List.of("abc", "def", "efg");
        abc.forEach(val -> System.out.println(">>" + val));
        //Method Reference
        abc. forEach(System.out::println);

    }

    /**
     * Stream의 map()과 flatMap의 차이점
     */
    List<Customer2> customers = List.of(new Customer2(101, "john", "john@gmail.com", Arrays.asList("397937955", "21654725")),
            new Customer2(102, "smith", "smith@gmail.com", Arrays.asList("89563865", "2487238947")),
            new Customer2(103, "peter", "peter@gmail.com", Arrays.asList("38946328654", "3286487236")),
            new Customer2(104, "kely", "kely@gmail.com", Arrays.asList("389246829364", "948609467")));

    @Test
    public void transFormUsingmap() {

        List<Object> emailList = customers.stream()  //Stream<Customer>
                .map(cust -> cust.getEmail())       //Stream<String>
                .collect(OBJECT_LIST_COLLECTOR);    //List<String>

        emailList.forEach(System.out::println);

        List<Object> phoneList = customers.stream()     //Stream<Customer>
                .map(cust -> cust.getPhoneNumbers())    //Stream<List<String>>
                .collect(Collectors.toList());          //List<List<String>>
        System.out.println(">"+phoneList);

        List<String> collect = customers.stream()                       //Stream<Customer>
                .flatMap(cust -> cust.getPhoneNumbers().stream())       //Stream<String>
                .collect(Collectors.toList());                          //List<String>
        System.out.println(">>"+collect);

    }


}
