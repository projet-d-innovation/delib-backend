package ma.enset.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record RoleUpdateRequestDTO(
        @NotBlank()
        String roleId,
        String roleName,
        List<Integer> permissions
) {
}
