package ma.enset.semestreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    @Builder
    public record Module (
        String codeModule,
        String intituleModule,
        BigDecimal coefficientModule,
        String codeSemestre
    ) {}
}
