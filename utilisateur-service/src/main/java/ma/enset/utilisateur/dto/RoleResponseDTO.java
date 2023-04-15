package ma.enset.utilisateur.dto;

import lombok.Builder;
import ma.enset.utilisateur.dto.PermissionResponseDTO;

import java.util.List;

@Builder
public record RoleResponseDTO(
        String roleId,
        String roleName,
        List<PermissionResponseDTO> permissions
) {
}
