package ma.enset.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record PermissionUpdateRequestDTO(
        @NotEmpty()
        int permissionId,
        @NotBlank()
        String permissionName,
        @NotBlank()
        String path,
        @NotBlank()
        String method
) {
}
