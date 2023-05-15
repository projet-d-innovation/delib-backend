package ma.enset.utilisateur.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class DepartementResponse {
    String codeDepartement;
    String intituleDepartement;
    String codeChefDepartement;

}