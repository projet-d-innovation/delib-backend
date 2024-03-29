package ma.enset.inscriptionpedagogique.dto;


import lombok.Builder;
import ma.enset.inscriptionpedagogique.model.EtatInscriptionPedagogique;

@Builder
public record InscriptionResponse (

    String id,
    EtudiantResponse etudiant,
    String codeFiliere,
    String codeSessionUniversitaire,
    int annee,
    EtatInscriptionPedagogique etat,
    float note

) { }
