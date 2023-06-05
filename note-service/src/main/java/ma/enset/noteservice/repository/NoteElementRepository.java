package ma.enset.noteservice.repository;

import ma.enset.noteservice.model.NoteElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface NoteElementRepository extends JpaRepository<NoteElement, NoteElement.NoteElementId> {

    List<NoteElement> findBySessionId(String sessionId);

    List<NoteElement> findBySessionIdIn(Set<String> sessionIdList);

    List<NoteElement> findBySessionIdAndCodeElement(String sessionId, String codeElement);

    List<NoteElement> findBySessionIdAndCodeElementIn(String sessionId, Set<String> codeElementList);

    @Transactional
    void deleteBySessionId(String sessionId);
    
    @Transactional
    void deleteBySessionIdIn(Set<String> sessionIdList);

    @Transactional
    void deleteBySessionIdAndCodeElement(String sessionId, String codeElement);

    @Transactional
    void deleteBySessionIdAndCodeElementIn(String sessionId, Set<String> codeElementList);

}
