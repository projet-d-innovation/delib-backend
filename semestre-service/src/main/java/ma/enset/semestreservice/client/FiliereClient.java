package ma.enset.semestreservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/filieres")
public interface FiliereClient {
    @GetExchange(url="/{codeFiliere}")
    ResponseEntity<Object> getFiliereById(@PathVariable("codeFiliere") String codeFiliere);

    @GetExchange(url="/bulk")
    ResponseEntity<Object> getAllFilieresByIds(@RequestParam("codeFiliere") Set<String> codeFiliere);
}
