package ma.enset.moduleservice;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.moduleservice.model.Module;
import ma.enset.moduleservice.repository.ModuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final ModuleRepository moduleRepository;

    @Override
    public void run(String... args) {

        List<Module> modules = List.of(
                Module.builder()
                        .codeModule("TII11106")
                        .codeSemestre("TII11006")
                        .intituleModule("MATHÉMATIQUES 1")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII11206")
                        .codeSemestre("TII11006")
                        .intituleModule("MATHÉMATIQUES 2")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII11306")
                        .codeSemestre("TII11006")
                        .intituleModule("TECHNIQUES DE PROGRAMMATION")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII11406")
                        .codeSemestre("TII11006")
                        .intituleModule("BASES DE DONNÉES")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII11506")
                        .codeSemestre("TII11006")
                        .intituleModule("TECHNOLOGIE DES ORDINATEURS ET RÉSEAUX")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII11606")
                        .codeSemestre("TII11006")
                        .intituleModule("ENVIRONNEMENTS ÉCONOMIQUE ET JURIDIQUE")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII11706")
                        .codeSemestre("TII11006")
                        .intituleModule("LANGUE ET TECHNIQUES D’EXPRESSION 1")
                        .coefficientModule(1)
                        .build(),
                // è------------------è
                Module.builder()
                        .codeModule("TII12106")
                        .codeSemestre("TII12006")
                        .intituleModule("CONCEPTION ORIENTÉE OBJET 1")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII12206")
                        .codeSemestre("TII12006")
                        .intituleModule("MATHEMATIQUES 3")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII12306")
                        .codeSemestre("TII12006")
                        .intituleModule("STRUCTURES DE DONNEES")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII12406")
                        .codeSemestre("TII12006")
                        .intituleModule("PROGRAMMATION ORIENTEE OBJET 1")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII12506")
                        .codeSemestre("TII12006")
                        .intituleModule("TECHNOLOGIE WEB")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII12606")
                        .codeSemestre("TII12006")
                        .intituleModule("SYSTÈMES D'EXPLOITATION")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII12706")
                        .codeSemestre("TII12006")
                        .intituleModule("LANGUES ET TECHNIQUES D’EXPRESSION 2")
                        .coefficientModule(1)
                        .build(),
                Module.builder()
                        .codeModule("TII12806")
                        .codeSemestre("TII12006")
                        .intituleModule("PROJET PERSONNEL")
                        .coefficientModule(1)
                        .build()
        );

        log.debug("Seeding Modules...");
        moduleRepository.saveAll(modules);
        log.debug("Modules seeded successfully.");
    }
}
