package ma.enset.semestreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SemestreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemestreServiceApplication.class, args);
    }

}
