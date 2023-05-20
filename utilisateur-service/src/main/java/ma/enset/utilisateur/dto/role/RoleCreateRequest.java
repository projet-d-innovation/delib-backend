package ma.enset.utilisateur.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;

@Builder
public record RoleCreateRequest(
        @NotBlank()
        String roleId,
        @NotBlank()
        String roleName,
        String groupe,
        Set<Integer> permissions
) {
}
