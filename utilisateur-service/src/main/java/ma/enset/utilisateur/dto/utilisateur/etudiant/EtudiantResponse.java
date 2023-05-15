package ma.enset.utilisateur.dto.utilisateur.etudiant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
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
public class EtudiantResponse {
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

}


