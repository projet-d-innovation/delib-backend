package ma.enset.utilisateur.dto.utilisateur.professeur;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.enset.utilisateur.dto.DepartementResponse;
import ma.enset.utilisateur.dto.ElementResponse;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class ProfesseurResponse {
    private String code;
    private String nom;
    private String prenom;
    private String telephone;
    private String pays;
    private String photo;
    private String codeDepartement;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DepartementResponse departement;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ElementResponse> elements;

}
