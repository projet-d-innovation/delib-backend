package ma.enset.semestreservice;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.semestreservice.model.Semestre;
import ma.enset.semestreservice.repository.SemestreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final SemestreRepository semestreRepository;

    @Override
    public void run(String... args) {

        List<Semestre> semestres = List.of(
                Semestre.builder()
                        .codeSemestre("TII11006")
                        .codeFiliere("GLSID")
                        .intituleSemestre("Semestre 1")
                        .build(),
                Semestre.builder()
                        .codeSemestre("TII12006")
                        .codeFiliere("GLSID")
                        .intituleSemestre("Semestre 2")
                        .build(),
                Semestre.builder()
                        .codeSemestre("TII13006")
                        .codeFiliere("GLSID")
                        .intituleSemestre("Semestre 3")
                        .build(),
                Semestre.builder()
                        .codeSemestre("TII14006")
                        .codeFiliere("GLSID")
                        .intituleSemestre("Semestre 4")
                        .build(),
                Semestre.builder()
                        .codeSemestre("TII15006")
                        .codeFiliere("GLSID")
                        .intituleSemestre("Semestre 5")
                        .build(),
                Semestre.builder()
                        .codeSemestre("TII16006")
                        .codeFiliere("GLSID")
                        .intituleSemestre("Semestre 6")
                        .build()
        );

        log.debug("Seeding Semestres...");
        semestreRepository.saveAll(semestres);
        log.debug("Semestres seeded successfully.");
    }
}
