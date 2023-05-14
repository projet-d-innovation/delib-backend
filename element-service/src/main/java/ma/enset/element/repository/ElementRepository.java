package ma.enset.element.repository;

import ma.enset.element.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ElementRepository extends JpaRepository<Element, String> {
    List<Element> findAllByCodeModule(String codeModule);

    List<Element> findAllByCodeModuleIn(Set<String> codesModule);

    List<Element> findAllByCodeProfesseur(String codeProfesseur);

    List<Element> findAllByCodeProfesseurIn(Set<String> codeProfesseur);
}
