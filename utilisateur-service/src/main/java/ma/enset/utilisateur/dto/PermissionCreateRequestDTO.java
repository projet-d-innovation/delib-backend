package ma.enset.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PermissionCreateRequestDTO(
        @NotBlank()
        String permissionName,
        @NotBlank()
        String path,
        @NotBlank()
        String method
) {
}
