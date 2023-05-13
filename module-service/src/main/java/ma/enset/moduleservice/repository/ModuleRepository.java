package ma.enset.moduleservice.repository;

import ma.enset.moduleservice.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {
    List<Module> findAllByCodeSemestre(String codeSemestre);
}
