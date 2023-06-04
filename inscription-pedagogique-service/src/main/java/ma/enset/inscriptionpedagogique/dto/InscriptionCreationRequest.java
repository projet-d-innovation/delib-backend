package ma.enset.inscriptionpedagogique.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import ma.enset.inscriptionpedagogique.model.EtatInscriptionPedagogique;

@Builder
public record InscriptionCreationRequest (

    @NotBlank
    String codeEtudiant,

    @NotBlank
    String codeFiliere,

    @NotBlank
    String codeSessionUniversitaire,

    @Positive
    int annee,

    @NotNull
    EtatInscriptionPedagogique etat,

    @PositiveOrZero
    float note

) { }
