package ma.enset.semestreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SemestreServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SemestreServiceApplication.class, args);
    }
}
