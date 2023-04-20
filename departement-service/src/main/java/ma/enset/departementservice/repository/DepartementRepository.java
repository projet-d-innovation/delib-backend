package ma.enset.departementservice.repository;

import ma.enset.departementservice.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository

public interface DepartementRepository extends JpaRepository<Departement, String> {
    boolean existsByCodeDepartement(String codeDepartement);
    Optional<Departement> findByCodeDepartement(String codeDepartement);

    @Transactional
    void deleteByCodeDepartement(String codeDepartement);

}
