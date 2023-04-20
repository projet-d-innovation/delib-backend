package ma.enset.filiereservice.dto;

public record RegleResponse(
        String codeRegle,

        float noteValidationModule,
        float noteEliminatoireModule,
        float noteCompensationModule,
        float noteValidationAnnee,
        int nbrModulesDerogation
) { }
