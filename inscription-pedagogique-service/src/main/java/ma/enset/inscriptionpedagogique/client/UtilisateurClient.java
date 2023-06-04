package ma.enset.inscriptionpedagogique.client;

import ma.enset.inscriptionpedagogique.dto.EtudiantResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/utilisateurs")
public interface UtilisateurClient {

    @GetExchange(url = "/exists")
    void allEtudiantsExist(@RequestParam("codes") Set<String> codes);

    @GetExchange(url = "/bulk")
    ResponseEntity<List<EtudiantResponse>> getAllEtudiantsByIds(@RequestParam("codes") Set<String> codes);

}
