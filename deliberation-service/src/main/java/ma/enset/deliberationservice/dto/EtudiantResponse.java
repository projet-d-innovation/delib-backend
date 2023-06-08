package ma.enset.deliberationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EtudiantResponse {

    private String code;
    private String cne;
    private String nom;
    private String prenom;
    private String cin;
    private String telephone;
    private String adresse;
    private Date dateNaissance;
    private String ville;
    private String pays;
    private String photo;
    
}