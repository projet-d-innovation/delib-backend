package ma.enset.moduleservice.repository;

import ma.enset.moduleservice.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {
    List<Module> findAllByCodeSemestre(String codeSemestre);

    List<Module> findAllByCodeSemestreIn(Set<String> codesSemestre);

    @Transactional
    void deleteAllByCodeSemestre(String codeSemestre);

    @Transactional
    void deleteAllByCodeSemestreIn(Set<String> codesSemestre);
}
