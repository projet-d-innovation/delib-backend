package ma.enset.noteservice.repository;

import ma.enset.noteservice.model.NoteElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NoteElementRepository extends JpaRepository<NoteElement, NoteElement.NoteElementId> {

    List<NoteElement> findBySessionId(String sessionId);

    List<NoteElement> findBySessionIdIn(Set<String> sessionIdList);

}
