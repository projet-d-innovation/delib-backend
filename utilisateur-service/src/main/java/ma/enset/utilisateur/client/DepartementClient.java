package ma.enset.utilisateur.client;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import ma.enset.utilisateur.dto.DepartementResponse;
import ma.enset.utilisateur.dto.ElementResponse;
import ma.enset.utilisateur.dto.GroupedElementsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/departements")
public interface DepartementClient {
    @GetExchange(url = "/exists")
    ResponseEntity<Void> existsAll(@RequestParam("codesDepartement") Set<String> codesDepartement);

    @GetExchange(url = "/{codeDepartement}")
    ResponseEntity<DepartementResponse> get(@PathVariable("codeDepartement") String codeDepartement);

    @GetExchange(url = "/bulk")
    ResponseEntity<List<DepartementResponse>> getAllById(@RequestParam("codeDepartementList") Set<String> codeDepartementList);
}
