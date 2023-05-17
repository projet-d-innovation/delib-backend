package ma.enset.semestreservice.repository;

import ma.enset.semestreservice.model.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface SemestreRepository extends JpaRepository<Semestre, String> {
    List<Semestre> findAllByCodeFiliere(String codeFiliere);

    List<Semestre> findAllByCodeFiliereIn(Set<String> codesFiliere);

    @Transactional
    void deleteAllByCodeFiliere(String CodeFiliere);

    @Transactional
    void deleteAllByCodeFiliereIn(Set<String> CodesFiliere);
}
