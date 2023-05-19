package ma.enset.utilisateur.repository;

import ma.enset.utilisateur.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    Page<Utilisateur> findAllByRoles_Groupe_AndNomContainsIgnoreCase(String groupe, String nom, Pageable pageable);
    
    Page<Utilisateur> findAllByRoles_RoleId_AndNomContainsIgnoreCase(String roleId, String nom, Pageable pageable);
}
