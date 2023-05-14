package ma.enset.filiereservice.repository;

import ma.enset.filiereservice.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, String> {

    List<Filiere> findAllByCodeDepartement(String codeDepartement);

    List<Filiere> findAllByCodeDepartementIn(Set<String> codeDepartements);

    List<Filiere> findAllByCodeRegleDeCalcul(String regleDeCalcul);

    List<Filiere> findAllByCodeRegleDeCalculIn(Set<String> regleDeCalcul);
}
