package ma.enset.deliberationservice.dto;

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
public class ModuleResponse {
    private String codeModule;
    private String intituleModule;
    private BigDecimal coefficientModule;
    private String codeSemestre;

}