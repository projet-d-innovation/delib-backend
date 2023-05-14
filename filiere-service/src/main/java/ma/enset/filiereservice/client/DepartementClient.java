package ma.enset.filiereservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/departements")
public interface DepartementClient {
    @GetExchange(url = "/exists")
    ResponseEntity<Void> exists(@RequestParam("codesDepartement") Set<String> codesDepartement);
}
