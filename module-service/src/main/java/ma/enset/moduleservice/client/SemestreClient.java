package ma.enset.moduleservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/semestres")
public interface SemestreClient {
    @GetExchange(url="/exists")
    ResponseEntity<Void> semestresExist(@RequestParam("codesSemestre") Set<String> codesSemestre);
}
