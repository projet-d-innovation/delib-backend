package ma.enset.semestreservice.repository;

import ma.enset.semestreservice.model.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemestreRepository extends JpaRepository<Semestre, String> {
    boolean existsByCodeFiliere(String code);
}
