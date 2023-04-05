package ma.enset.element.repository;

import ma.enset.element.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementRepository extends JpaRepository<Element, String> {
    List<Element> findByCodeModule(String codeModule);

    List<Element> findByCodeProfesseur(String codeProfesseur);
}
