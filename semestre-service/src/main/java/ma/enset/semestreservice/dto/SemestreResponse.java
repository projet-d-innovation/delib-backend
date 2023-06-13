package ma.enset.semestreservice.dto;

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
    private List<Module> modules;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FiliereResponse filiere;

    @Builder
    public record Module(
            String codeModule,
            String intituleModule,
            BigDecimal coefficientModule,
            String codeSemestre
    ) {
    }


}
