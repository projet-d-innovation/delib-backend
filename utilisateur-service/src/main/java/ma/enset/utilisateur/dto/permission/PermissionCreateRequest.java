package ma.enset.utilisateur.dto.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PermissionCreateRequest(
        @NotBlank()
        String permissionName,
        String path,
        String method,
        String groupe
) {
}
