package ma.enset.noteservice.repository;

import ma.enset.noteservice.model.NoteModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface
NoteModuleRepository extends JpaRepository<NoteModule, String> {

}
