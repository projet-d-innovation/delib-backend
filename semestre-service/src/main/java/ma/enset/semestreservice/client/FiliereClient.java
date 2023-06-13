package ma.enset.semestreservice.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import ma.enset.semestreservice.dto.FiliereResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/filieres")
public interface FiliereClient {
    @GetExchange(url = "/{codeFiliere}")
    ResponseEntity<FiliereResponse> getFiliereById(
            @PathVariable("codeFiliere") String codeFiliere,
            @RequestParam(name = "includeDepartement", defaultValue = "false") boolean includeDepartement,
            @RequestParam(name = "includeSemestre", defaultValue = "false") boolean includeSemestre,
            @RequestParam(name = "includeRegleDeCalcule", defaultValue = "false") boolean includeRegleDeCalcule,
            @RequestParam(name = "includeChefFiliere", defaultValue = "false") boolean includeChefFiliere
    );

    @GetExchange(url = "/exists")
    ResponseEntity<Void> existsAll(@RequestParam("codesFiliere") Set<String> codesFiliere);

    @GetExchange(url = "/bulk")
    ResponseEntity<List<FiliereResponse>> getAllFilieresByIds(
            @RequestParam("codeFiliere") Set<String> codeFiliere,
            @RequestParam(name = "includeDepartement", defaultValue = "false") boolean includeDepartement,
            @RequestParam(name = "includeSemestre", defaultValue = "false") boolean includeSemestre,
            @RequestParam(name = "includeRegleDeCalcule", defaultValue = "false") boolean includeRegleDeCalcule,
            @RequestParam(name = "includeChefFiliere", defaultValue = "false") boolean includeChefFiliere
    );
}
