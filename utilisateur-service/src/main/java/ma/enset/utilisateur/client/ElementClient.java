package ma.enset.utilisateur.client;


import ma.enset.utilisateur.dto.ElementResponse;
import ma.enset.utilisateur.dto.GroupedElementsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/elements")
public interface ElementClient {
    @GetExchange(url = "/professeur/{codeProfesseur}")
    ResponseEntity<List<ElementResponse>> getAllByCodeProfesseur(
            @PathVariable("codeProfesseur") String codeProfesseur
    );

    @GetExchange(url = "/professeur/bulk")
    ResponseEntity<List<GroupedElementsResponse>> getAllByCodesProfesseur(
            @RequestParam("codesProfesseur") Set<String> codesProfesseur
    );


    @PatchExchange("/professeur")
    ResponseEntity<Void> handleProfesseurDeletion(
            @RequestParam("codesProfesseur") Set<String> codesProfesseur
    );
}
