package ma.enset.sessionuniversitaireservice.dto;

import ma.enset.sessionuniversitaireservice.model.EtatSessionUniversitaire;

import java.time.LocalDate;

public record SessionResponse(
    String id,
    LocalDate startDate,
    LocalDate endDate,
    EtatSessionUniversitaire etat
) { }
