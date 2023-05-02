package ma.enset.moduleservice.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.moduleservice.model.Module;
import ma.enset.moduleservice.repository.ModuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final ModuleRepository moduleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Module > modules = List.of(
                Module.builder()
                        .codeModule("M1")
                        .intituleModule("Module 1")
                        .coefficientModule(0.6f)
                        .codeSemestre("S1")
                        .build(),
                Module.builder()
                        .codeModule("M2")
                        .intituleModule("Module 2")
                        .coefficientModule(0.4f)
                        .codeSemestre("S1")
                        .build()
        );

        log.info("Database seeding: started");
        moduleRepository.saveAll(modules);
        log.info("Database seeding: completed with success");
    }
}
