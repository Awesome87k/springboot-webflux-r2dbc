package myspringboot.reactive.webflux;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTest {

    @Test
    public void subscriptionTest() {
        Flux<Integer> integerFlux = Flux.range(1, 100);//.log();

        integerFlux.subscribe(
                value -> System.out.println("value : "+value)
                , err -> err.printStackTrace()
                , () -> System.out.println(">>>>>>>Complete<<<<<<<<<")
                , subscription -> subscription.request(10)
        );

        StepVerifier.create(integerFlux)
                .expectNextCount(10)
                .expectComplete();
    }

    @Test
    public void thenReqeustTest() {
        Flux<Integer> flux = Flux.just(101, 201, 301).log();

        //test case
        StepVerifier.create(flux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(101)
                .thenRequest(2)
                .expectNext(201, 301)
                .verifyComplete();
    }

    @Test
    public void cancleCallbackTest() {
        Flux<Integer> integerFlux = Flux.range(1, 100).log();

        //cancel됐을때 처리하는 방법
        integerFlux.doOnCancel(() -> System.out.println("Cancle Method Invoked"))
                .doOnComplete(() -> System.out.println("=== Complete"))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                        try {
                            Thread.sleep(500);
                            request(1);
                            System.out.println("value" + value);
                            if(value == 5) {
                                cancel();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

        StepVerifier.create(integerFlux)
                .expectNext(1, 2, 3, 4, 5)
                .thenCancel()
                .verify();
    }
}
