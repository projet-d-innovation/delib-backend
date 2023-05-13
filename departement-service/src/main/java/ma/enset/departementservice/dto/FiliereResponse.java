package ma.enset.filiereservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class FiliereResponse {
    private String codeFiliere;
    private String intituleFiliere;
    private String codeDepartement;
    private String codeChefFiliere;
    private String codeRegleDeCalcul;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Semestre> semestres;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ChefFiliere chefFiliere;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RegleDeCalculResponse regleDeCalcul;

    @Builder
    public record ChefFiliere(
            String code,
            String nom,
            String prenom,
            String telephone,
            String photo
    ) {
    }

    @Builder
    public record Semestre(
            String codeSemestre,
            String intituleSemestre
    ) {
    }

    @Builder
    public record RegleDeCalculResponse(
            String codeRegleDeCalcul,
            BigDecimal noteValidationModule,
            BigDecimal noteEliminatoireModule,
            BigDecimal noteCompensationModule,
            BigDecimal noteValidationAnnee,
            int nbrModulesDerogation
    ) {
    }

}
