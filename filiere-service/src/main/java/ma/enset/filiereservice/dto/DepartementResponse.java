package ma.enset.filiereservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class DepartementResponse {
    String codeDepartement;
    String intituleDepartement;
    String codeChefDepartement;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UtilisateurResponse chefDepartement;

}