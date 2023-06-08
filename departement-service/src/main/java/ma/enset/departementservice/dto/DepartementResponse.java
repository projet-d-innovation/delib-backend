package ma.enset.departementservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

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
    private List<FiliereResponse> filieres;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UtilisateurResponse chefDepartement;

}