package ma.enset.deliberationservice.dto.session;

import lombok.*;
import ma.enset.deliberationservice.model.SessionResult;
import ma.enset.deliberationservice.model.SessionType;

import java.math.BigDecimal;


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

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<SemestreResponse> semestres;
//
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private UtilisateurResponse chefFiliere;

}
