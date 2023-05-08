package ma.enset.sessionuniversitaireservice.repository;

import ma.enset.sessionuniversitaireservice.model.SessionUniversitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionUniversitaireRepository extends JpaRepository<SessionUniversitaire, String> {
}
