package ma.enset.inscriptionpedagogique.client;

import ma.enset.inscriptionpedagogique.exception.ApiClientException;
import ma.enset.inscriptionpedagogique.exception.InternalErrorException;
import ma.enset.inscriptionpedagogique.exception.handler.dto.ExceptionResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
public class WebClientConfig {

    private final String UTILISATEUR_SERVICE_NAME = "UTILISATEUR-SERVICE";
    private final String FILIERE_SERVICE_NAME = "FILIERE-SERVICE";
    private final String SESSION_UNIVERSITAIRE_SERVICE_NAME = "SESSION-UNIVERSITAIRE-SERVICE";

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
    public UtilisateurClient utilisateurClient(WebClient.Builder builder) {
        WebClient utilisateurWebClient = builder
            .baseUrl("http://" + UTILISATEUR_SERVICE_NAME)
            .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(utilisateurWebClient))
            .build();

        return factory.createClient(UtilisateurClient.class);
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
    public SessionUniversitaireClient sessionUniversitaireClient(WebClient.Builder builder) {
        WebClient sessionUniversitaireWebClient = builder
            .baseUrl("http://" + SESSION_UNIVERSITAIRE_SERVICE_NAME)
            .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(sessionUniversitaireWebClient))
            .build();

        return factory.createClient(SessionUniversitaireClient.class);
    }
}
