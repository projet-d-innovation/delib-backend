package ma.enset.moduleservice.client;

import ma.enset.moduleservice.dto.SemestreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/semestres")
public interface SemestreClient {
    @GetExchange(url = "/exists")
    ResponseEntity<Void> semestresExist(@RequestParam("codesSemestre") Set<String> codesSemestre);

    @GetExchange(url = "/{codeSemestre}")
    ResponseEntity<SemestreResponse> getById(
            @PathVariable("codeSemestre") String codeSemestre,
            @RequestParam(name = "includeFiliere", defaultValue = "false") boolean includeFiliere,
            @RequestParam(name = "includeModules", defaultValue = "false") boolean includeModules
    );

    @GetExchange(url = "/bulk")
    ResponseEntity<List<SemestreResponse>> getAllByIds(
            @RequestParam("codesSemestre") Set<String> codesSemestre,
            @RequestParam(name = "includeFiliere", defaultValue = "false") boolean includeFiliere,
            @RequestParam(name = "includeModules", defaultValue = "false") boolean includeModules
    );
}
