package ma.enset.moduleservice.dto;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Element> elements;


    @Builder
    public record Element (
        String codeElement,
        String intituleElement,
        float coefficientElement
    ) {}
}