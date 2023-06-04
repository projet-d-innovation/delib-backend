package ma.enset.inscriptionpedagogique.repository;

import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface InscriptionPedagogiqueRepository extends JpaRepository<InscriptionPedagogique, Long> {

    List<InscriptionPedagogique> findAllByCodeEtudiant(String codeEtudiant);
    
    @Transactional
    void deleteAllByCodeEtudiant(String codeEtudiant);

    @Transactional
    void deleteAllByCodeEtudiantIn(Set<String> codesEtudiant);

}
