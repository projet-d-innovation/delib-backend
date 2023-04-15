package ma.enset.utilisateur.dto;

import lombok.Builder;

@Builder
public record PermissionUpdateRequestDTO(
        int permissionId,
        String permissionName,
        String path,
        String method
) {
}
