package ma.enset.deliberationservice.dto.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.enset.deliberationservice.dto.GroupedNotesModuleResponse;
import ma.enset.deliberationservice.dto.InscriptionResponse;
import ma.enset.deliberationservice.dto.NoteModuleResponse;
import ma.enset.deliberationservice.model.SessionResult;
import ma.enset.deliberationservice.model.SessionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class SessionResponse {
    private String idSession;
    private String idInscription;
    private String codeSemestre;
    private SessionType sessionType;
    private BigDecimal note;
    private SessionResult sessionResult;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<NoteModuleResponse> notes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InscriptionResponse inscription;
}
