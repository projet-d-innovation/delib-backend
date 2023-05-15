package ma.enset.utilisateur.dto.role;

import lombok.Builder;

import java.util.List;

@Builder
public record RoleUpdateRequest(
        String roleId,
        String roleName,
        List<Integer> permissions
) {
}
