package ma.enset.filiereservice.repository;

import ma.enset.filiereservice.model.RegleDeCalcul;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegleDeCalculRepository extends JpaRepository<RegleDeCalcul, String> {
}
