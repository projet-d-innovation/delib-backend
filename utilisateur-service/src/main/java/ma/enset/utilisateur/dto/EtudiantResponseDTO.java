package ma.enset.utilisateur.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record EtudiantResponseDTO(
        String code,
        String cin,
        String cne,
        String nom,
        String prenom,
        String telephone,
        String adresse,
        Date dateNaissance,
        String ville,
        String pays,
        String photo
) {
}
