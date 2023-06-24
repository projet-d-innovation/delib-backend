package ma.enset.departementservice;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.repository.DepartementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final DepartementRepository departementRepository;

    @Override
    public void run(String... args) {

        List<Departement> departements = List.of(
                Departement.builder()
                        .codeDepartement("GM")
                        .intituleDepartement("Génie Mécanique")
                        .build(),
                Departement.builder()
                        .codeDepartement("GE")
                        .intituleDepartement("Génie Electrique")
                        .build(),
                Departement.builder()
                        .codeDepartement("MI")
                        .intituleDepartement("Mathématiques et Informatique")
                        .codeChefDepartement("DAAIF_AZIZ")
                        .build()
        );

        log.debug("Seeding departements...");
        departementRepository.saveAll(departements);
        log.debug("Departements seeded successfully.");
    }
}
