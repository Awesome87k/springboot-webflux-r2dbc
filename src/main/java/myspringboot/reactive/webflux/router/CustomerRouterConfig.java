package myspringboot.reactive.webflux.router;

import myspringboot.reactive.webflux.handler.CustomerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * MVC Controller에 해당
 */
@Configuration
public class CustomerRouterConfig {
    @Autowired
    private CustomerHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route()
                .GET("/router/customers", handler::loadCustomerStream)
                .GET("/router/customers/{id}", handler::findCustomer)
                .POST("/router/customers", handler::saveCustomer)
                .build();
    }

}
