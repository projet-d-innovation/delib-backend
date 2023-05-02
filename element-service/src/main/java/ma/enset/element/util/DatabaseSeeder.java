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

    @Override
    public void run(String... args) throws Exception {
        List<Element> elements = List.of(
                Element.builder()
                        .codeElement("E1")
                        .intituleElement("Element 1")
                        .coefficientElement(0.6f)
                        .codeModule("M1")
                        .build(),
                Element.builder()
                        .codeElement("E2")
                        .intituleElement("Element 2")
                        .coefficientElement(0.4f)
                        .codeModule("M1")
                        .build(),
                Element.builder()
                        .codeElement("E3")
                        .intituleElement("Element 3")
                        .coefficientElement(0.6f)
                        .codeModule("M2")
                        .build(),
                Element.builder()
                        .codeElement("E4")
                        .intituleElement("Element 4")
                        .coefficientElement(0.4f)
                        .codeModule("M2")
                        .build()
        );
        log.info("Database seeding: started");

        elementRepository.saveAll(elements);

        log.info("Database seeding: completed with success");

    }
}
