package ma.enset.departementservice.client;


import lombok.extern.slf4j.Slf4j;
import ma.enset.departementservice.exception.ApiClientException;
import ma.enset.departementservice.exception.InternalErrorException;
import ma.enset.departementservice.exception.handler.dto.ExceptionResponse;
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
    private final String FILIERE_SERVICE_NAME = "FILIERE-SERVICE";
    private final String UTILISATEUR_SERVICE_NAME = "UTILISATEUR-SERVICE";

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
    public FiliereClient filiereClient(WebClient.Builder builder) {
        WebClient filiereWebClient = builder
                .baseUrl("http://" + FILIERE_SERVICE_NAME)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(filiereWebClient))
                .build();

        return factory.createClient(FiliereClient.class);
    }

    @Bean
    public UtilisateurClient utilisateurClient(WebClient.Builder builder) {
        WebClient utilisateurWebClient = builder
                .baseUrl("http://" + UTILISATEUR_SERVICE_NAME)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(utilisateurWebClient))
                .build();

        return factory.createClient(UtilisateurClient.class);
    }

}