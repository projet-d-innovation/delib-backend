package ma.enset.filiereservice.dto;

import java.math.BigDecimal;

public record RegleDeCalculResponse(
        String codeRegleDeCalcul,
        BigDecimal noteValidationModule,
        BigDecimal noteEliminatoireModule,
        BigDecimal noteCompensationModule,
        BigDecimal noteValidationAnnee,
        int nbrModulesDerogation
) {
}
