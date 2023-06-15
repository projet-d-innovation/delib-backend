package ma.enset.inscriptionpedagogique.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<InscriptionResponse> inscriptions;

}