package ma.enset.noteservice.repository;

import ma.enset.noteservice.model.NoteModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface
NoteModuleRepository extends JpaRepository<NoteModule, String> {

    List<NoteModule> findByCodeSession(String codeSession);
}
