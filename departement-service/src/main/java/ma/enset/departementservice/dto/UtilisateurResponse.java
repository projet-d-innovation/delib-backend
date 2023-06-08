package ma.enset.departementservice.dto;

import lombok.*;

import java.util.Date;


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
    private String sexe;
    private String codeDepartement;
    private String codeFiliere;
}