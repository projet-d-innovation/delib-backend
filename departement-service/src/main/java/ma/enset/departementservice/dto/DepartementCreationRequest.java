package ma.enset.departementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartementCreationRequest(
        @NotBlank
        String codeDepartement,
        @NotBlank
        String intituleDepartement ,
        String codeChefDepartement
) {}
