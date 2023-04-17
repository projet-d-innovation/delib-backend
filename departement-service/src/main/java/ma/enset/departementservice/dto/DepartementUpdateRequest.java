package ma.enset.departementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartementUpdateRequest(
        @NotBlank
        String intituleDepartement
) {}
