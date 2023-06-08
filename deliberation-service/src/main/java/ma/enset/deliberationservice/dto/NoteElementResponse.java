package ma.enset.deliberationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoteElementResponse {
    private String codeElement;
    private String sessionId;
    private BigDecimal note;
    private boolean redoublant;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ElementResponse element;
}