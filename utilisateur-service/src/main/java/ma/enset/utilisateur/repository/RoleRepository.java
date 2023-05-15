package ma.enset.utilisateur.repository;

import ma.enset.utilisateur.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("select r from Role r join r.permissions p where p.permissionId = ?1")
    List<Role> findAllByPermissionId(Integer permissionId);

    @Query("select r from Role r join r.permissions p where p.permissionId in ?1")
    List<Role> findAllByPermissionIdIn(Set<Integer> permissionIds);
}
