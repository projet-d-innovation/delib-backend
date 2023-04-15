package ma.enset.utilisateur.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RoleUpdateRequestDTO(
        String roleId,
        String roleName,
        List<Integer> permissions
) {
}
