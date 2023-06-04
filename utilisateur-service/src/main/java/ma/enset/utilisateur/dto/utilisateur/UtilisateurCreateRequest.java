package ma.enset.utilisateur.dto.utilisateur;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record UtilisateurCreateRequest(
        @NotBlank()
        String code,
        String cin,
        String cne,
        @NotBlank()
        String nom,
        @NotBlank()
        String prenom,
        @NotBlank()
        String telephone,
        String adresse,
        LocalDate dateNaissance,
        String pays,
        String ville,
        @Size(max = 1)
        String sexe,
        String photo,
        String codeDepartement,
        Set<String> roles

) {
}
