package ma.enset.inscriptionpedagogique.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class EtudiantResponse {

    private final String code;
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