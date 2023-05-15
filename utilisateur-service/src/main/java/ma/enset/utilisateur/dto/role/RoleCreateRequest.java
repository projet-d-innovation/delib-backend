package ma.enset.utilisateur.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record RoleCreateRequest(
        @NotBlank()
        String roleId,
        @NotBlank()
        String roleName,
        Set<Integer> permissions
) {
}
