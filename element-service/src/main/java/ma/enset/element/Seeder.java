package ma.enset.element;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.element.model.Element;
import ma.enset.element.repository.ElementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final ElementRepository elementRepository;

    @Override
    public void run(String... args) {

        List<Element> elements = List.of(
                // ------------------------
                Element.builder()
                        .codeElement("TII11116")
                        .codeModule("TII11106")
                        .intituleElement("Analyse Numérique")
                        .coefficientElement(0.5f)
                        .build(),
                Element.builder()
                        .codeElement("TII11126")
                        .codeModule("TII11106")
                        .intituleElement("Algèbre Linéaire")
                        .coefficientElement(0.5f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII11216")
                        .codeModule("TII11206")
                        .intituleElement("Probabilité")
                        .coefficientElement(0.4f)
                        .build(),
                Element.builder()
                        .codeElement("TII11226")
                        .codeModule("TII11206")
                        .intituleElement("Statistique")
                        .coefficientElement(0.6f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII11316")
                        .codeModule("TII11306")
                        .intituleElement("Algorithmique")
                        .coefficientElement(0.5f)
                        .build(),
                Element.builder()
                        .codeElement("TII11326")
                        .codeModule("TII11306")
                        .intituleElement("Programmation en langage C")
                        .coefficientElement(0.5f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII11416")
                        .codeModule("TII11406")
                        .intituleElement("Modèle entité association")
                        .coefficientElement(0.6f)
                        .build(),
                Element.builder()
                        .codeElement("TII11426")
                        .codeModule("TII11406")
                        .intituleElement("SGBDR/SQL")
                        .coefficientElement(0.4f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII11516")
                        .codeModule("TII11506")
                        .intituleElement("Architecture des ordinateurs et assembleur")
                        .coefficientElement(0.6f)
                        .build(),
                Element.builder()
                        .codeElement("TII11526")
                        .codeModule("TII11506")
                        .intituleElement("Techniques de base pour les réseaux")
                        .coefficientElement(0.4f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII11616")
                        .codeModule("TII11606")
                        .intituleElement("Economie et organisation des entreprises")
                        .coefficientElement(0.5f)
                        .build(),
                Element.builder()
                        .codeElement("TII11626")
                        .codeModule("TII11606")
                        .intituleElement("Initiation au droit")
                        .coefficientElement(0.5f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII11716")
                        .codeModule("TII11706")
                        .intituleElement("Français 1")
                        .coefficientElement(0.5f)
                        .build(),
                Element.builder()
                        .codeElement("TII11726")
                        .codeModule("TII11706")
                        .intituleElement("Anglais 1")
                        .coefficientElement(0.5f)
                        .build(),
                // ------------------------
                // zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
                // ------------------------
                Element.builder()
                        .codeElement("TII12116")
                        .codeModule("TII12106")
                        .intituleElement("Conception Orienté Objet")
                        .coefficientElement(0.8f)
                        .build(),
                Element.builder()
                        .codeElement("TII12126")
                        .codeModule("TII12106")
                        .intituleElement("Projet Conception Orientée Objet")
                        .coefficientElement(0.2f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII12216")
                        .codeModule("TII12206")
                        .intituleElement("Recherche opérationnelle")
                        .coefficientElement(0.8f)
                        .build(),
                Element.builder()
                        .codeElement("TII12226")
                        .codeModule("TII12206")
                        .intituleElement("Théorie de graphe")
                        .coefficientElement(0.2f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII12316")
                        .codeModule("TII12306")
                        .intituleElement("Structures de données")
                        .coefficientElement(0.8f)
                        .build(),
                Element.builder()
                        .codeElement("TII12326")
                        .codeModule("TII12306")
                        .intituleElement("Projet Structures de données")
                        .coefficientElement(0.2f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII12416")
                        .codeModule("TII12406")
                        .intituleElement("Programmation orientée objet avec le langage C++")
                        .coefficientElement(0.8f)
                        .build(),
                Element.builder()
                        .codeElement("TII12426")
                        .codeModule("TII12406")
                        .intituleElement("Projet programmation orientée objet avec le langage C++")
                        .coefficientElement(0.2f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII12516")
                        .codeModule("TII12506")
                        .intituleElement("Développement web")
                        .coefficientElement(0.8f)
                        .build(),
                Element.builder()
                        .codeElement("TII12526")
                        .codeModule("TII12506")
                        .intituleElement("Projet Développement web")
                        .coefficientElement(0.2f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII12616")
                        .codeModule("TII12606")
                        .intituleElement("Théorie des systèmes d’exploitation")
                        .coefficientElement(0.3f)
                        .build(),
                Element.builder()
                        .codeElement("TII12626")
                        .codeModule("TII12606")
                        .intituleElement("Systèmes d’exploitation Windows/Unix/Linux")
                        .coefficientElement(0.7f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII12716")
                        .codeModule("TII12706")
                        .intituleElement("Français 2")
                        .coefficientElement(0.5f)
                        .build(),
                Element.builder()
                        .codeElement("TII12726")
                        .codeModule("TII12706")
                        .intituleElement("Anglais 2")
                        .coefficientElement(0.5f)
                        .build(),
                // ------------------------
                Element.builder()
                        .codeElement("TII12816")
                        .codeModule("TII12806")
                        .intituleElement("PROJET PERSONNEL")
                        .coefficientElement(1f)
                        .build()
        );

        log.debug("Seeding Elements...");
        elementRepository.saveAll(elements);
        log.debug("Elements seeded successfully.");
    }
}
