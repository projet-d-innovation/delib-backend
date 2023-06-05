package ma.enset.noteservice.repository;

import ma.enset.noteservice.model.NoteModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NoteModuleRepository extends JpaRepository<NoteModule, NoteModule.NoteModuleId> {

    List<NoteModule> findBySessionId(String sessionId);

    List<NoteModule> findBySessionIdIn(Set<String> sessionIdList);

}
