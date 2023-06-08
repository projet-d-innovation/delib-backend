package ma.enset.deliberationservice.repository;

import ma.enset.deliberationservice.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    List<Session> findAllByIdInscriptionIn(List<String> idsInscription);
}
