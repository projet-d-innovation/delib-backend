package ma.enset.utilisateur.dto;

import lombok.Builder;
import ma.enset.utilisateur.dto.RoleResponseDTO;

import java.util.List;

@Builder
public record UtilisateurNestedRolesResponseDTO(
        String code,
        String nom,
        String prenom,
        String telephone,
        List<RoleResponseDTO> roles
) {
}
