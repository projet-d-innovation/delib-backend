package ma.enset.filiereservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegleDeCalcul {
    @Id
    private String codeRegle;

    @DecimalMin("0.0")
    @DecimalMax("20.00")
    private BigDecimal noteValidationModule;
    @DecimalMin("0.0")
    @DecimalMax("20.00")
    private BigDecimal noteEliminatoireModule;
    @DecimalMin("0.0")
    @DecimalMax("20.00")
    private BigDecimal noteCompensationModule;
    @DecimalMin("0.0")
    @DecimalMax("20.00")
    private BigDecimal noteValidationAnnee;
    @Min(0)
    private int nbrModulesDerogation;
    @OneToMany(mappedBy = "regleDeCalcul", fetch = FetchType.LAZY, targetEntity = Filiere.class)
    private List<Filiere> filieres;



}
