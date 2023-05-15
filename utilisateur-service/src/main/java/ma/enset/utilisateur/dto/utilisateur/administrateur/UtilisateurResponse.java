package ma.enset.utilisateur.dto.utilisateur.administrateur;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.enset.utilisateur.dto.DepartementResponse;
import ma.enset.utilisateur.dto.FiliereResponse;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.role.RoleResponse;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class UtilisateurResponse {
    private String code;
    private String nom;
    private String prenom;
    private String telephone;
    private String photo;

    private String codeDepartement;
    private String codeFiliere;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RoleResponse> roles;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PermissionResponse> permissions;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DepartementResponse departementResponse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FiliereResponse filiereResponse;

}