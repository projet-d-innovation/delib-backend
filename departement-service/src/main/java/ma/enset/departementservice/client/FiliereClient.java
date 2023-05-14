package ma.enset.departementservice.client;

import ma.enset.departementservice.dto.FiliereByDepartementResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/filieres")
public interface FiliereClient {
    @GetExchange(url = "/departement/{codeDepartement}")
    ResponseEntity<FiliereByDepartementResponse> getFilieresByCodeDepartement(
            @PathVariable("codeDepartement") String codeDepartement,
            @RequestParam("includeSemestre") boolean includeSemestre,
            @RequestParam("includeRegleDeCalcule") boolean includeRegleDeCalcule,
            @RequestParam("includeChefFiliere") boolean includeChefFiliere
    );

    @GetExchange(url = "/departement/bulk")
    ResponseEntity<List<FiliereByDepartementResponse>> getFilieresByCodesDepartement(
            @RequestParam("codeDepartements") Set<String> codeDepartements,
            @RequestParam("includeSemestre") boolean includeSemestre,
            @RequestParam("includeRegleDeCalcule") boolean includeRegleDeCalcule,
            @RequestParam("includeChefFiliere") boolean includeChefFiliere
    );

    @DeleteExchange("/bulk")
    ResponseEntity<?> deleteAll(@RequestParam("codeFiliere") Set<String> codeFiliere);
}
