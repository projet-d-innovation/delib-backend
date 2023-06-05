package ma.enset.noteservice.dto.notemodule;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.noteservice.dto.ModuleResponse;
import ma.enset.noteservice.dto.noteelement.NoteElementResponse;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoteModuleResponse {
    private String codeModule;
    private String sessionId;
    private BigDecimal note;
    private boolean redoublant;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ModuleResponse module;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<NoteElementResponse> notesElement;

}