package ma.enset.deliberationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
public class DeliberationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliberationServiceApplication.class, args);
    }

}
