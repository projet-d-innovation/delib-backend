package ma.enset.utilisateur.dto.utilisateur;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.enset.utilisateur.dto.DepartementResponse;
import ma.enset.utilisateur.dto.ElementResponse;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.role.RoleResponse;

import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class UtilisateurResponse {
    private String code;
    private String cin;
    private String cne;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private Date dateNaissance;
    private String ville;
    private String pays;
    private String photo;
    private String codeDepartement;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RoleResponse> roles;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PermissionResponse> permissions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DepartementResponse departement;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ElementResponse> elements;

}