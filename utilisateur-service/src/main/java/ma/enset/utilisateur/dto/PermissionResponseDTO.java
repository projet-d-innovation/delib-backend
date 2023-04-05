package ma.enset.utilisateur.dto;

import lombok.Builder;

@Builder
public record PermissionResponseDTO(
        int permissionId,
        String permissionName,
        String path,
        String method
) {
}
