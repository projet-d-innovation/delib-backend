package ma.enset.semestreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class FiliereResponse {
    private String codeFiliere;
    private String intituleFiliere;
    private String codeDepartement;
    private String codeChefFiliere;
    private String codeRegleDeCalcul;

}
