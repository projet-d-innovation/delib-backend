package ma.enset.utilisateur.repository;

import jakarta.transaction.Transactional;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    @Query("SELECT t FROM  Utilisateur t WHERE (LOWER(t.code) LIKE  LOWER(CONCAT('%',:search,'%')) OR LOWER(t.nom) LIKE  LOWER(CONCAT('%',:search,'%')) OR LOWER(t.prenom) LIKE  LOWER(CONCAT('%',:search,'%')) OR LOWER(t.cin) LIKE LOWER(CONCAT('%',:search,'%')) OR LOWER(t.cne) LIKE LOWER(CONCAT('%',:search,'%'))) AND (:role MEMBER OF t.roles)")
    Page<Utilisateur> findAllWithSearchAndRole(@Param("search") String search,@Param("role") Role role, Pageable pageable);
    @Query("SELECT t FROM  Utilisateur t WHERE LOWER(t.code) LIKE  LOWER(CONCAT('%',:search,'%')) OR LOWER(t.nom) LIKE  LOWER(CONCAT('%',:search,'%')) OR LOWER(t.prenom) LIKE  LOWER(CONCAT('%',:search,'%')) ")
    Page<Utilisateur> findAllWithSearch(@Param("search") String search, Pageable pageable);

    boolean existsByCode(String code);

    Optional<Utilisateur> findByCode(String code);

    @Transactional
    void deleteByCode(String code);

}
