package ma.enset.element.client;


import ma.enset.element.dto.ProfesseurResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url="/api/v1/utilisateurs")
public interface UtilisateurClient {
    @GetExchange(url="professeurs/{codeProfesseur}")
    ResponseEntity<ProfesseurResponse> getProfesseur(@PathVariable("codeProfesseur") String codeProfesseur);

    @GetExchange(url="professeurs/bulk")
    ResponseEntity<List<ProfesseurResponse>> findByCodes(@RequestParam("codes") List<String> codes);
}
