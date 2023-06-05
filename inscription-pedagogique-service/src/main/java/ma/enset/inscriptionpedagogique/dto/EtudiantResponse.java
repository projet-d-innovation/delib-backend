package ma.enset.inscriptionpedagogique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate dateNaissance;
    private String ville;
    private String pays;
    private String photo;
    
}