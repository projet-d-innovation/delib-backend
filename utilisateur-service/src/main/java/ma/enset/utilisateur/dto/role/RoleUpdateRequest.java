package ma.enset.utilisateur.dto.role;

import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record RoleUpdateRequest(
        String roleId,
        String roleName,
        Set<Integer> permissions
) {
}
