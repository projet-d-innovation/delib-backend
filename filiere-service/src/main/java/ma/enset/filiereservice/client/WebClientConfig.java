package ma.enset.filiereservice.client;


import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.exception.ApiClientException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.exception.handler.dto.ExceptionResponse;
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
    private final String DEPARTEMENT_SERVICE_NAME = "DEPARTEMENT-SERVICE";
    private final String SEMESTRE_SERVICE_NAME = "SEMESTRE-SERVICE";

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
    public SemestreClient semestreClient(WebClient.Builder builder) {
        WebClient semestreWebClient = builder
                .baseUrl("http://" + SEMESTRE_SERVICE_NAME)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(semestreWebClient))
                .build();

        return factory.createClient(SemestreClient.class);
    }

}
