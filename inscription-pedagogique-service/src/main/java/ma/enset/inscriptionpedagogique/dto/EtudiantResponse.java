package ma.enset.inscriptionpedagogique.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EtudiantResponse (

    String code,
    String cne,
    String nom,
    String prenom,
    String cin,
    String telephone,
    String adresse,
    LocalDate dateNaissance,
    String ville,
    String pays,
    String photo
    
) { }