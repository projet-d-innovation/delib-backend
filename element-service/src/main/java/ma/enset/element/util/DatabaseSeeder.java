package ma.enset.element.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.element.model.Element;
import ma.enset.element.repository.ElementRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final ElementRepository elementRepository;
    private final Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {
        List<Element> productList = IntStream.rangeClosed(1, 100)
                .mapToObj(i ->
                        Element.builder()
                                .codeElement(faker.aws().accountId())
                                .codeModule(faker.aws().accountId())
                                .intituleElement(faker.commerce().productName() + Instant.now().getNano())
                                .coefficientElement(Math.round((float) (Math.random() * 0.8 + 0.1) * 10.0f) / 10.0f)
                                .codeProfesseur(faker.aws().accountId())
                                .build()
                ).toList();

        log.info("Database seeding: started");

        elementRepository.saveAll(productList);

        log.info("Database seeding: completed with success");

    }
}
