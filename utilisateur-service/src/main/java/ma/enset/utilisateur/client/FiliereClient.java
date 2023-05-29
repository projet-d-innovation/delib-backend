package ma.enset.utilisateur.client;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/filieres")
public interface FiliereClient {
    @PatchExchange(url = "/utilisateur")
    ResponseEntity<Void> handleChefFiliereDeletion(
            @RequestParam("codesUtilisateur") Set<String> codesUtilisateur
    );
}
