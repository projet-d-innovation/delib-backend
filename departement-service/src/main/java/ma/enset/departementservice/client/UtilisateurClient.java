package ma.enset.departementservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/utilisateurs")
public interface UtilisateurClient {
    @PatchExchange(url = "/departement/bulk")
    ResponseEntity<Void> handleKeyDepartementDeletion(
            @RequestParam("codesDepartement") Set<String> codesDepartement
    );

    @GetExchange(url = "/exists")
    ResponseEntity<Void> existsById(
            @RequestParam("codes") Set<String> codes
    );

}
