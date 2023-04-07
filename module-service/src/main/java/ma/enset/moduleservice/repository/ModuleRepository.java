package ma.enset.moduleservice.repository;

import ma.enset.moduleservice.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {
    Optional<Module> findByCodeModule(String codeModule);
    boolean existsByCodeModule(String codeModule);
    @Transactional
    void deleteByCodeModule(String codeModule);
}
