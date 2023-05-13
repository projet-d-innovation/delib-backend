package ma.enset.filiereservice.model;

import jakarta.persistence.Column;
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
    @Column(unique = true, nullable = false, updatable = false)
    private String codeRegleDeCalcul;
    private float noteValidationModule;
    private float noteEliminatoireModule;
    private float noteCompensationModule;
    private float noteValidationAnnee;
    private int nbrModulesDerogation;

}
