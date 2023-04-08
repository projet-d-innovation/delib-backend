package ma.enset.filiereservice.repository;

import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.model.RegleDeCalcul;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, String> {

    boolean existsByCodeDepartement(String codeDepartement);
    boolean existsByRegleDeCalcul(RegleDeCalcul regleDeCalcul);


}
