package ma.enset.filiereservice.repository;

import ma.enset.filiereservice.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, String> {
    boolean existsByCodeRegle(String codeRegle);

    boolean existsByCodeDepartement(String codeDepartement);


}
