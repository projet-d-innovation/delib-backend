package ma.enset.element.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class WebClientConfig {
    private final String MODULE_SERVICE_NAME = "MODULE-SERVICE";
    private final String UTILISATEUR_SERVICE_NAME = "UTILISATEUR-SERVICE";


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ModuleClient elementClient(WebClient.Builder builder) {
        WebClient elementWebClient = builder
                .baseUrl("http://" + MODULE_SERVICE_NAME)
                .filter(logRequest())
                .filter(errorHandlingFilter())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(elementWebClient))
                .build();

        return factory.createClient(ModuleClient.class);
    }

    @Bean
    public UtilisateurClient utilisateurClient(WebClient.Builder builder) {
        WebClient utilisateurWebClient = builder
                .baseUrl("http://" + UTILISATEUR_SERVICE_NAME)
                .filter(logRequest())
                .filter(errorHandlingFilter())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(utilisateurWebClient))
                .build();

        return factory.createClient(UtilisateurClient.class);
    }


    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.debug("Request: {} {}", request.method(), request.url());
            request.headers().forEach((name, values) -> values.forEach(value -> log.debug("{}={}", name, value)));
            return Mono.just(request);
        });
    }

    private static ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                return response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Error response: {}", errorBody);
                            return Mono.error(new HttpClientErrorException(response.statusCode(), "response.statusCode().getReasonPhrase()", response.headers().asHttpHeaders(), errorBody.getBytes(), Charset.defaultCharset()));
                        });
            } else {
                return Mono.just(response);
            }
        });
    }
}
