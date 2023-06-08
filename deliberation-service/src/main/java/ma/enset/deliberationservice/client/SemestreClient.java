package ma.enset.deliberationservice.client;

import ma.enset.deliberationservice.dto.SemestreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/semestres")
public interface SemestreClient {
    @GetExchange(url = "/exists")
    ResponseEntity<Void> exists(@RequestParam("codesSemestre") Set<String> codesSemestre);

    @GetExchange(url = "/{codeSemestre}")
    ResponseEntity<SemestreResponse> getById(
            @PathVariable("codeSemestre") String codeSemestre,
            @RequestParam(name = "includeModules", defaultValue = "false") boolean includeModules
    );
}
