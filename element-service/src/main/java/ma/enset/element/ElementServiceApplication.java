package ma.enset.element;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ElementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElementServiceApplication.class, args);
    }
}
