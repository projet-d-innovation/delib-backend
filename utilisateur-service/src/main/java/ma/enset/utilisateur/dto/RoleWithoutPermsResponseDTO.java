package ma.enset.utilisateur.dto;

import lombok.Builder;

@Builder
public record RoleWithoutPermsResponseDTO(
        String roleId,
        String roleName
) {
}
