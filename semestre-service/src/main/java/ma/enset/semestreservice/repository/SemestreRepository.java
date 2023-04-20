package ma.enset.semestreservice.repository;

import ma.enset.semestreservice.model.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SemestreRepository extends JpaRepository<Semestre, String> {
    boolean existsByCodeSemestre(String codeSemestre);
    Optional<Semestre> findByCodeSemestre(String codeSemestre);

    @Transactional
    void deleteByCodeSemestre(String codeSemestre);}
