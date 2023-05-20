package ma.enset.utilisateur.dto.permission;

import lombok.Builder;

@Builder
public record PermissionResponse(
        int permissionId,
        String permissionName,
        String path,
        String method,
        String groupe
) {
}
