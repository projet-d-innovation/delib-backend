package ma.enset.utilisateur.repository;

import jakarta.transaction.Transactional;
import ma.enset.utilisateur.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.permissions")
    List<Role> findAllWithPermissions();


    boolean existsByRoleId(String roleId);

    Optional<Role> findByRoleId(String roleId);

    @Transactional
    void deleteByRoleId(String roleId);
}
