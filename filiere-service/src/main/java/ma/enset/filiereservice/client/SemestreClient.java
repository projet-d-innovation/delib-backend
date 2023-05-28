package ma.enset.filiereservice.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import ma.enset.filiereservice.dto.GroupedSemestresResponse;
import ma.enset.filiereservice.dto.SemestreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/semestres")
public interface SemestreClient {
    @GetExchange(url = "/exists")
    ResponseEntity<Void> exists(@RequestParam("codesDepartement") Set<String> codesSemestre);

    @GetExchange(url = "/filiere/{codeFiliere}")
    ResponseEntity<List<SemestreResponse>> getAllByCodeFiliere(@PathVariable("codeFiliere") String codeFiliere);

    @GetExchange(url = "/filiere/bulk")
    ResponseEntity<List<GroupedSemestresResponse>> getAllByCodeFilieres(@RequestParam("codesFiliere") Set<String> codesFiliere);

    @DeleteExchange(url = "/filiere/bulk")
    ResponseEntity<Void> deleteAllByCodesFiliere(@RequestBody @NotEmpty Set<@NotBlank String> codesFiliere);

    @DeleteExchange(url = "/filiere/{codeFiliere}")
    ResponseEntity<Void> deleteAllByCodeFiliere(@PathVariable("codeFiliere") String codeFiliere);
}
