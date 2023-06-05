package ma.enset.gateway;


import com.netflix.discovery.DiscoveryClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        r -> r.path("/eureka/web")
                                .filters(f -> f.setPath("/"))
                                .uri("http://localhost:8761")
                )
                .route(
                        r -> r.path("/eureka/**")
                                .uri("http://localhost:8761")
                )
                .route(
                        r -> r.path("/api/v1/utilisateurs/**")
                                .or()
                                .path("/api/v1/roles/**")
                                .or()
                                .path("/api/v1/permissions/**")
                                .or()
                                .path("/utilisateur-service/docs")
                                .filters(f -> f.rewritePath("/utilisateur-service/docs", "/v3/api-docs"))
                                .uri("lb://utilisateur-service")
                )
                .route(
                        r -> r.path("/api/v1/elements/**")
                                .or()
                                .path("/element-service/docs")
                                .filters(f -> f.rewritePath("/element-service/docs", "/v3/api-docs"))
                                .uri("lb://element-service")
                )
                .route(
                        r -> r.path("/api/v1/modules/**")
                                .or()
                                .path("/module-service/docs")
                                .filters(f -> f.rewritePath("/module-service/docs", "/v3/api-docs"))
                                .uri("lb://module-service")
                )
                .route(
                        r -> r.path("/api/v1/departements/**")
                                .or()
                                .path("/departement-service/docs")
                                .filters(f -> f.rewritePath("/departement-service/docs", "/v3/api-docs"))
                                .uri("lb://departement-service")
                )
                .route(
                        r -> r.path("/api/v1/filieres/**")
                                .and()
                                .path("/api/v1/regledecalcul/**")
                                .or()
                                .path("/filiere-service/docs")
                                .filters(f -> f.rewritePath("/filiere-service/docs", "/v3/api-docs"))
                                .uri("lb://filiere-service")
                )
                .route(
                        r -> r.path("/api/v1/semestres/**")
                                .or()
                                .path("/semestre-service/docs")
                                .filters(f -> f.rewritePath("/semestre-service/docs", "/v3/api-docs"))
                                .uri("lb://semestre-service")
                )
                .route(
                        r -> r.path("/api/v1/session/**")
                                .or()
                                .path("/deliberation-service/docs")
                                .filters(f -> f.rewritePath("/deliberation-service/docs", "/v3/api-docs"))
                                .uri("lb://deliberation-service")
                )
                .build();
    }
}
