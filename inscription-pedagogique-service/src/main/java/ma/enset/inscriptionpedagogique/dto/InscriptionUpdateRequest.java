package ma.enset.inscriptionpedagogique.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import ma.enset.inscriptionpedagogique.model.EtatInscriptionPedagogique;
import ma.enset.inscriptionpedagogique.validationgroups.OnBulkUpdate;

@Builder
public record InscriptionUpdateRequest (

    @NotNull(groups = OnBulkUpdate.class)
    Long id,

    EtatInscriptionPedagogique etat,

    @PositiveOrZero
    Float note

) { }
