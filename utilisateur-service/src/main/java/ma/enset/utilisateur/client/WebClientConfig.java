package ma.enset.utilisateur.client;


import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.exception.ApiClientException;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.handler.dto.ExceptionResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class WebClientConfig {
    private final String ELEMENT_SERVICE_NAME = "ELEMENT-SERVICE";
    private final String DEPARTEMENT_SERVICE_NAME = "DEPARTEMENT-SERVICE";
    private final String FILIERE_SERVICE_NAME = "FILIERE-SERVICE";

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
    public ElementClient elementClient(WebClient.Builder builder) {
        WebClient elementWebClient = builder
                .baseUrl("http://" + ELEMENT_SERVICE_NAME)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(elementWebClient))
                .build();

        return factory.createClient(ElementClient.class);
    }

    @Bean
    public DepartementClient departementClient(WebClient.Builder builder) {
        WebClient departementWebClient = builder
                .baseUrl("http://" + DEPARTEMENT_SERVICE_NAME)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(departementWebClient))
                .build();

        return factory.createClient(DepartementClient.class);
    }

    @Bean
    public FiliereClient filiereClient(WebClient.Builder builder) {
        WebClient filiereWebClient = builder
                .baseUrl("http://" + FILIERE_SERVICE_NAME)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(filiereWebClient))
                .build();

        return factory.createClient(FiliereClient.class);
    }
}
