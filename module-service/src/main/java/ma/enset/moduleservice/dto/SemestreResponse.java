package ma.enset.moduleservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SemestreResponse {
    private String codeSemestre;
    private String codeFiliere;
    private String intituleSemestre;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Filiere filiere;

    @Builder
    public record Filiere(
            String codeFiliere,
            String intituleFiliere,
            String codeDepartement,
            String codeChefFiliere,
            String codeRegleDeCalcul
    ) {
    }
}
