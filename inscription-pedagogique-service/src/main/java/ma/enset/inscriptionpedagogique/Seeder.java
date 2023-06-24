package ma.enset.inscriptionpedagogique;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.inscriptionpedagogique.model.EtatInscriptionPedagogique;
import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;
import ma.enset.inscriptionpedagogique.repository.InscriptionPedagogiqueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final InscriptionPedagogiqueRepository repository;

    @Override
    public void run(String... args) {

        List<InscriptionPedagogique> inscriptionPedagogiques = List.of(
                InscriptionPedagogique.builder()
                        .id("2021-2023-ahmed.elbouchouki-1")
                        .annee(1)
                        .note(12.514f)
                        .codeEtudiant("ahmed.elbouchouki")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.VALIDE)
                        .build(),
                InscriptionPedagogique.builder()
                        .id("2021-2023-ahmed.elbouchouki-2")
                        .annee(2)
                        .codeEtudiant("ahmed.elbouchouki")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.EN_COURS)
                        .build(),
// ------------------------------------------------------------------------
                InscriptionPedagogique.builder()
                        .id("2021-2023-aymane.ouahmane-1")
                        .annee(1)
                        .note(15.124f)
                        .codeEtudiant("aymane.ouahmane")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.VALIDE)
                        .build(),
                InscriptionPedagogique.builder()
                        .id("2021-2023-aymane.ouahmane-2")
                        .annee(2)
                        .codeEtudiant("aymane.ouahmane")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.EN_COURS)
                        .build(),
// ------------------------------------------------------------------------
                InscriptionPedagogique.builder()
                        .id("2021-2023-mohammed.benchelbikh-1")
                        .annee(1)
                        .note(18.431f)
                        .codeEtudiant("mohammed.benchelbikh")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.VALIDE)
                        .build(),
                InscriptionPedagogique.builder()
                        .id("2021-2023-mohammed.benchelbikh-2")
                        .annee(2)
                        .codeEtudiant("mohammed.benchelbikh")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.EN_COURS)
                        .build(),
// ------------------------------------------------------------------------
                InscriptionPedagogique.builder()
                        .id("2021-2023-yacoub.benhamama-1")
                        .annee(1)
                        .note(13.123f)
                        .codeEtudiant("yacoub.benhamama")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.VALIDE)
                        .build(),
                InscriptionPedagogique.builder()
                        .id("2021-2023-yacoub.benhamama-2")
                        .annee(2)
                        .codeEtudiant("yacoub.benhamama")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.EN_COURS)
                        .build(),
// ------------------------------------------------------------------------
                InscriptionPedagogique.builder()
                        .id("2021-2023-zakaria.elmourtazak-1")
                        .annee(1)
                        .note(9.431f)
                        .codeEtudiant("zakaria.elmourtazak")
                        .codeSessionUniversitaire("2021-2023")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.NON_VALIDE)
                        .build(),
                InscriptionPedagogique.builder()
                        .id("2022-2024-zakaria.elmourtazak-1")
                        .annee(1)
                        .codeEtudiant("zakaria.elmourtazak")
                        .codeSessionUniversitaire("2022-2024")
                        .codeFiliere("GLSID")
                        .etat(EtatInscriptionPedagogique.EN_COURS)
                        .build()
        );

        log.debug("Seeding InscriptionPedagogique...");
        repository.saveAll(inscriptionPedagogiques);
        log.debug("InscriptionPedagogique seeded successfully.");
    }
}
