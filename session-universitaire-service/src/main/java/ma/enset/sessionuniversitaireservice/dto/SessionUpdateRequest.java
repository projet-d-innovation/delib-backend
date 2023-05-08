package ma.enset.sessionuniversitaireservice.dto;

import jakarta.validation.constraints.NotNull;
import ma.enset.sessionuniversitaireservice.model.EtatSessionUniversitaire;

public record SessionUpdateRequest(
    @NotNull
    EtatSessionUniversitaire etat
) {}
