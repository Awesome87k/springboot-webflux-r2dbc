package myspringboot.reactive.webflux;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FLuxTest {
    @Test
    public void justFlux() {
        Flux<String> stringFlux = Flux.just("hello", "World").log();    //.log는 부하의 원인이 될수있어 개발단계에서만 사용한다
//        stringFlux.subscribe(val -> System.out.println(">>"+val));
//        stringFlux.subscribe(System.out::println);

//        StepVerifier flux에 대한 테스트코드
        StepVerifier.create(stringFlux)
                .expectNext("hello")
                .expectNext("World")
                .verifyComplete();
    }

    @Test
    public void errorFlux() {
        Flux<String> stringFlux = Flux.just("Boot", "MSA")
                .concatWithValues("Cloud")
                .concatWith(Flux.error(new RuntimeException("Exception 발생")))
                .concatWithValues("Reactive Mongo")     //exception 발생이후는 해당데이터는 찍히지 않음..
                .log();

//        stringFlux.subscribe(System.out::println);
        stringFlux.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));

        //StepVerifier flux에 대한 테스트코드
        StepVerifier.create(stringFlux)
                .expectNext("Boot")
                .expectNext("MSA")
                .expectNext("Cloud")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void subscribeFlux() {
        Flux<String> stringFlux = Flux.just("hello", "webflux", "reactive");
//        System.out.println(stringFlux); 이건 확인불가임

        //백프레셔
        stringFlux.subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
//                s.request(Long.MAX_VALUE);  //최대 요청갯수
                s.request(2);
            }

            @Override
            public void onNext(String s) {
                System.out.println("FluxTest.onNext:" + s);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("FluxTest.onError:" + t);
            }

            @Override
            public void onComplete() {
                System.out.println("FluxTest.onComplete");
            }
        });

        StepVerifier.create(stringFlux)
                .expectNext("hello")
                .expectNext("webflux")
                .expectComplete();
//                .verifyComplete();

    }

    @Test
    public void rangeFlux() {
        Flux<Integer> range = Flux.range(10, 10)
                .filter(num -> Math.floorMod(num, 2) == 1)
                .log();

//        range.subscribe(System.out::println);

        StepVerifier.create(range)
                .expectNext(11, 13, 15, 17, 19)
                .verifyComplete();
    }

}
