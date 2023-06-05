package ma.enset.noteservice.dto.noteelement;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.enset.noteservice.dto.ElementResponse;

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