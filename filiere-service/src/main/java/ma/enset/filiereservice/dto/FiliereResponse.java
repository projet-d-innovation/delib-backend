package ma.enset.filiereservice.dto;

import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.Min;

import java.util.List;

public record FiliereResponse(
        String codeFiliere,

        String intituleFiliere,

        String codeRegle,

        String codeDepartement ,

        String codeChefFiliere ,

         int nombreEtudiants,

         int nombreSemestres,
         List<String> semestreIds,
        List<String> anneeUniversitaireIds
) { }
