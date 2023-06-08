package ma.enset.deliberationservice.dto;


import lombok.Builder;

@Builder
public record InscriptionResponse(

        String id,
        EtudiantResponse etudiant,
        String codeFiliere,
        String codeSessionUniversitaire,
        int annee,
        String etat,
        float note

) {
}
