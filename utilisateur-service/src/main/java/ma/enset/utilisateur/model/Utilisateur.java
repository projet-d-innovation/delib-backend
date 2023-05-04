package ma.enset.utilisateur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.utilisateur.dto.DepartementResponse;
import ma.enset.utilisateur.dto.ElementResponse;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Utilisateur {
    @Id
    private String code;
    private String cin;
    private String cne;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String photo;
    private LocalDateTime dateNaissance;
    private String ville;
    private String pays = "Maroc";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "utilisateur_role",
            joinColumns = @JoinColumn(name = "utilisateurCode"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles;


    private String codeDepartement;

    @Transient
    private DepartementResponse Departement;
    @Transient
    private List<ElementResponse> elements;


}
