package ma.enset.utilisateur.dto;

import lombok.*;

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
