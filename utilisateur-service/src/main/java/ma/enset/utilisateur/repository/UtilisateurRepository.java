package ma.enset.utilisateur.repository;

import jakarta.transaction.Transactional;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    Optional<Utilisateur> findByCodeAndRolesContains(String code, Role role);


    Page<Utilisateur> findAllByRolesContains(Role role, Pageable pageable);


    boolean existsByCode(String code);

    Optional<Utilisateur> findByCode(String code);

    @Transactional
    void deleteByCode(String code);

}
