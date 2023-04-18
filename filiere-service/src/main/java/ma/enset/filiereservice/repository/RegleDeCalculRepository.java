package ma.enset.filiereservice.repository;

import ma.enset.filiereservice.model.RegleDeCalcul;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RegleDeCalculRepository extends JpaRepository<RegleDeCalcul,String> {
    boolean existsByCodeRegle(String codeRegleDeCalcul);
    Optional<RegleDeCalcul> findByCodeRegle(String codeRegleDeCalcul);

    @Transactional
    void deleteByCodeRegle(String codeRegleDeCalcul);
}
