package ma.enset.inscriptionpedagogique.repository;

import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionPedagogiqueRepository extends JpaRepository<InscriptionPedagogique, Long> {
    List<InscriptionPedagogique> findAllByCodeFiliereAndCodeSessionUniversitaireAndAnnee(
        String codeFilier, String codeSessionUniversitaire, int anne
    );

    List<InscriptionPedagogique> findAllByCodeEtudiant(String codeEtudiant);

}
