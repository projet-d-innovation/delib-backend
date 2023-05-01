package ma.enset.filiereservice.repository;

import ma.enset.filiereservice.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, String> {

    boolean existsByCodeFiliere(String codeFiliere);


    Optional<Filiere> findByCodeFiliere(String codeFiliere);

    List<Filiere> findAllByCodeDepartement(String codeDepartement);

    @Query("SELECT f FROM Filiere f JOIN f.regleDeCalcul r WHERE r.codeRegle = :codeRegle")
    List<Filiere> findAllByCodeRegle(@Param("codeRegle")String codeRegle);

    Optional<Filiere> findByCodeChefFiliere(String codeChefFiliere);



    void deleteByCodeFiliere(String codeFiliere);

    List<Filiere> findByCodeFiliereContaining(String codeFiliere);

    Optional<List<Filiere>> findAllByCodeChefFiliere(String codeChefFiliere);

}
