package ma.enset.sessionuniversitaireservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import ma.enset.sessionuniversitaireservice.model.EtatSessionUniversitaire;

import java.time.LocalDate;

public record SessionCreationRequest(
    @NotNull
    LocalDate startDate,
    @NotNull
    LocalDate endDate,
    EtatSessionUniversitaire etat
) {
    @JsonIgnore
    public String id() {
        return String.format("%d-%d", startDate.getYear(), endDate.getYear());
    }
}
