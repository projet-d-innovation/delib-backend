package ma.enset.inscriptionpedagogique.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/sessions-universitaire")
public interface SessionUniversitaireClient {

    @GetExchange(url = "/exists")
    void allSessionsUniversitaireExist(@RequestParam("ids") Set<String> ids);

}
