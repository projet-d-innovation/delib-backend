package ma.enset.departementservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record DepartementUpdateRequest(
        @NotBlank
        String intituleDepartement ,
        @NotBlank
        String codeChefDepartement

) {}
