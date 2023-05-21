package ma.enset.element.client;

import ma.enset.element.exception.ApiClientException;
import ma.enset.element.exception.InternalErrorException;
import ma.enset.element.exception.handler.dao.ExceptionResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration(proxyBeanMethods = false)
public class WebClientConfig {
    private final String MODULE_SERVICE_NAME = "MODULE-SERVICE";
    private final String USER_SERVICE_NAME = "USER-SERVICE";

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
            .defaultStatusHandler(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                                    .bodyToMono(ExceptionResponse.class)
                                    .map(ApiClientException::new)
            )
            .defaultStatusHandler(
                HttpStatusCode::is5xxServerError,
                clientResponse -> clientResponse
                                    .createException()
                                    .map(exception -> new InternalErrorException(
                                        exception.getMessage(), exception.getCause()
                                    ))
            );
    }

    @Bean
    public ModuleClient moduleClient(WebClient.Builder builder) {
        WebClient moduleWebClient = builder
                                        .baseUrl("http://" + MODULE_SERVICE_NAME)
                                        .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                            .builder(WebClientAdapter.forClient(moduleWebClient))
                                            .build();

        return factory.createClient(ModuleClient.class);
    }

    @Bean
    public UserClient userClient(WebClient.Builder builder) {
        WebClient userWebClient = builder
                                    .baseUrl("http://" + USER_SERVICE_NAME)
                                    .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                            .builder(WebClientAdapter.forClient(userWebClient))
                                            .build();

        return factory.createClient(UserClient.class);
    }
}
