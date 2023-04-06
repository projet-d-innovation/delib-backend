package ma.enset.filiereservice.dto;

public record RegleDeCalculResponseDTO(

        String codeRegle,

        float noteValidationModule,
        float noteEliminatoireModule,
        float noteCompensationModule,
        float noteValidationAnnee,
        int nbrModulesDerogation
) {
}
