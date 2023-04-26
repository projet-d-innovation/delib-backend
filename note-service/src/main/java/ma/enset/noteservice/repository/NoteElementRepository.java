package ma.enset.noteservice.repository;

import ma.enset.noteservice.model.NoteElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface
NoteElementRepository extends JpaRepository<NoteElement, String> {
    List<NoteElement> findByCodeSession(String codeSession);

    List<NoteElement> findAllByCodeElement(String codeElement);

    NoteElement findByCodeSessionAndCodeElement(String codeSession, String codeElement);
}
