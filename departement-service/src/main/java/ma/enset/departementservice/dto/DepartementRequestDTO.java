package ma.enset.departementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DepartementRequestDTO(
        @NotBlank
        String codeDepartement,
        @NotBlank
        String intituleDepartement ,
        @NotBlank
        String codeChefDepartement
) {
}
