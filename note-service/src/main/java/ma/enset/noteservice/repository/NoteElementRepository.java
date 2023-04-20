package ma.enset.noteservice.repository;

import ma.enset.noteservice.model.NoteElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
NoteElementRepository extends JpaRepository<NoteElement, String> {
//    Optional<NoteElement> findByNoteElementId(String noteElementId);
//    boolean existsByNoteElementId(String noteElementId);

//    NoteElement findByNoteElementId(String noteElementId);
//    Optional<NoteModule> findByCodeModule(String codeModule);
//    boolean existsByCodeModule(String codeModule);
//    @Transactional
//    void deleteByCodeModule(String codeModule);
}
