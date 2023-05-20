package ma.enset.utilisateur.dto.role;

import lombok.Builder;

import java.util.Set;

@Builder
public record RoleUpdateRequest(
        String roleId,
        String roleName,
        String groupe,
        Set<Integer> permissions
) {
}
