package ma.enset.utilisateur.model;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.utilisateur.dto.DepartementResponse;
import ma.enset.utilisateur.dto.ElementResponse;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Utilisateur implements Persistable<String> {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String code;
    private String cin;
    private String cne;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    private String telephone;
    private String adresse;
    @Column(length = 1)
    private String sexe;
    private String photo;
    private LocalDate dateNaissance;
    private String ville;
    private String pays = "Maroc";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "utilisateur_role",
            joinColumns = @JoinColumn(name = "utilisateurCode"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles;
    private String codeDepartement;

    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public String getId() {
        return code;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}
