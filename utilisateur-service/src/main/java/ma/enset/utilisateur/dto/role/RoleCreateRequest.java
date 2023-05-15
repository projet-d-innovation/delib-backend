package ma.enset.utilisateur.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record RoleCreateRequest(
        @NotBlank()
        String roleId,
        @NotBlank()
        String roleName,
        List<Integer> permissions
) {
}
