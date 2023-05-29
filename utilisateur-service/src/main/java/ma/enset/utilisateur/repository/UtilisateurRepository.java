package ma.enset.utilisateur.repository;

import ma.enset.utilisateur.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    Page<Utilisateur> findAllByRoles_Groupe_AndNomContainsIgnoreCase(String groupe, String nom, Pageable pageable);

    Page<Utilisateur> findAllByRoles_RoleId_AndNomContainsIgnoreCase(String roleId, String nom, Pageable pageable);

    @Query("select u from Utilisateur u join u.roles r where r.roleId = ?1")
    List<Utilisateur> findAllByRoleId(String roleId);

    @Query("select u from Utilisateur u join u.roles r where r.roleId in ?1")
    List<Utilisateur> findAllByRoleIdIn(Set<String> roleIds);

    List<Utilisateur> findAllByCodeDepartementIn(Set<String> codesDepartement);
}
