package ma.enset.departementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
public class DepartementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartementServiceApplication.class, args);
    }

}
