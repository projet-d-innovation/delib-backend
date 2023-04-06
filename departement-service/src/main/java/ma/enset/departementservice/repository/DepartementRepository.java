package ma.enset.departementservice.repository;

import ma.enset.departementservice.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DepartementRepository extends JpaRepository<Departement, String> {

}
