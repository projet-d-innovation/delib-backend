package ma.enset.element.repository;

import jakarta.transaction.Transactional;
import ma.enset.element.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElementRepository extends JpaRepository<Element, String> {

    Optional<Element> findByCodeElement(String codeModule);
    boolean existsByCodeElement(String codeModule);
    @Transactional
    void deleteByCodeElement(String codeModule);

    List<Element> findByCodeModule(String codeModule);

    List<Element> findByCodeProfesseur(String codeProfesseur);
}
