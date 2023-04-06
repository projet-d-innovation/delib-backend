package ma.enset.filiereservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegleDeCalcul {
    @Id
    private String codeRegle;

    private float noteValidationModule ;
    private float noteEliminatoireModule ;
    private float noteCompensationModule ;
    private float noteValidationAnnee;
    private int nbrModulesDerogation ;

}
