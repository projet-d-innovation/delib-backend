package ma.enset.sessionuniversitaireservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.sessionuniversitaireservice.model.EtatSessionUniversitaire;
import ma.enset.sessionuniversitaireservice.model.SessionUniversitaire;
import ma.enset.sessionuniversitaireservice.repository.SessionUniversitaireRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final SessionUniversitaireRepository repository;

    @Override
    public void run(String... args) {

        List<SessionUniversitaire> sessionUniversitaires = List.of(
                SessionUniversitaire.builder()
                        .startDate(java.time.LocalDate.of(2021, 9, 1))
                        .endDate(java.time.LocalDate.of(2023, 7, 1))
                        .id("2021-2023")
                        .etat(EtatSessionUniversitaire.EN_COURS)
                        .build(),
                SessionUniversitaire.builder()
                        .startDate(java.time.LocalDate.of(2022, 9, 1))
                        .endDate(java.time.LocalDate.of(2024, 7, 1))
                        .id("2022-2024")
                        .etat(EtatSessionUniversitaire.EN_COURS)
                        .build()
        );

        log.debug("Seeding SessionUniversitaire...");
        repository.saveAll(sessionUniversitaires);
        log.debug("SessionUniversitaire seeded successfully.");
    }
}
