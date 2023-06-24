package ma.enset.filiereservice;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.repository.FiliereRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final FiliereRepository filiereRepository;

    @Override
    public void run(String... args) {

        //.codeDepartement("GM")
        //.codeDepartement("GE")
        //.codeDepartement("MI")
        //.codeDepartement("GEG")
        //.codeDepartement("STAIC")

        List<Filiere> filieres = List.of(
                Filiere.builder()
                        .codeFiliere("GIL")
                        .codeDepartement("GM")
                        .intituleFiliere("Génie Industriel et Logistique")
                        .build(),
                Filiere.builder()
                        .codeFiliere("GMSI")
                        .codeDepartement("GM")
                        .intituleFiliere("Génie Mécanique des Systémes Industriels")
                        .build(),
                Filiere.builder()
                        .codeFiliere("MRMI")
                        .codeDepartement("GM")
                        .intituleFiliere("Mécanique, Robotique et Matériaux Innovants")
                        .build(),
                Filiere.builder()
                        .codeFiliere("SEER")
                        .codeDepartement("GE")
                        .intituleFiliere("Génie Electrique option : Systémes Electriques et Energies Renouvelables")
                        .build(),
                Filiere.builder()
                        .codeFiliere("GECSI")
                        .codeDepartement("GE")
                        .intituleFiliere("Génie Electrique et Controle des Systémes Industriels")
                        .build(),
                Filiere.builder()
                        .codeFiliere("GLSID")
                        .codeDepartement("MI")
                        .intituleFiliere("Génie du Logiciel et des Systémes Informatiques Distribués")
                        .codeChefFiliere("KHIAT_AZZDIN")
                        .build(),
                Filiere.builder()
                        .codeFiliere("II-BDCC")
                        .codeDepartement("MI")
                        .intituleFiliere("Ingénierie Informatique : Big Data et Cloud Computing")
                        .codeChefFiliere("AHRIZ_SOUAD")
                        .build(),
                Filiere.builder()
                        .codeFiliere("SDIA")
                        .codeDepartement("MI")
                        .intituleFiliere("Systémes Distribués et Intelligence Artificielle")
                        .build()
        );

        log.debug("Seeding fileres...");
        filiereRepository.saveAll(filieres);
        log.debug("Filieres seeded successfully.");
    }
}
