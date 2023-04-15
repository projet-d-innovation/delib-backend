package ma.enset.utilisateur.repository;

import jakarta.transaction.Transactional;
import ma.enset.utilisateur.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    boolean existsByPermissionId(int permissionId);

    Optional<Permission> findByPermissionId(int permissionId);

    @Transactional
    void deleteByPermissionId(int permissionId);

}
