package ma.enset.inscriptionpedagogique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InscriptionPedagogiqueApplication {
    public static void main(String[] args) {
        SpringApplication.run(InscriptionPedagogiqueApplication.class, args);
    }
}
