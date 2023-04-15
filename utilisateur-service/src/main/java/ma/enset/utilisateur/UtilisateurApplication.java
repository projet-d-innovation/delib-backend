package ma.enset.utilisateur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UtilisateurApplication {
    public static void main(String[] args) {
        SpringApplication.run(UtilisateurApplication.class, args);
    }
}
