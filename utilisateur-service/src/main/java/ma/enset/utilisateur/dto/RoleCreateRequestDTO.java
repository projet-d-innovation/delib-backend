package ma.enset.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record RoleCreateRequestDTO(
        @NotBlank()
        String roleId,
        @NotBlank()
        String roleName,
        List<Integer> permissions
) {
}
