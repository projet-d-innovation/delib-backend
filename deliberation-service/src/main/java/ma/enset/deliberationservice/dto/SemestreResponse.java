package ma.enset.deliberationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<ModuleResponse> modules;

}
