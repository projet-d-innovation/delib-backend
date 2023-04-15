package ma.enset.utilisateur.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UtilisateurResponseDTO(
        String code,
        String nom,
        String prenom,
        String telephone,

        String photo,
        List<RoleWithoutPermsResponseDTO> roles
) {
}
