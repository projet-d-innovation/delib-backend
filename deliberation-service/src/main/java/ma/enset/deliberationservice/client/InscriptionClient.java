package ma.enset.deliberationservice.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ma.enset.deliberationservice.dto.InscriptionResponse;
import ma.enset.deliberationservice.dto.RequiredSearchParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/inscriptions-pedagogique")
public interface InscriptionClient {
    @GetExchange(url = "/search")
    ResponseEntity<List<InscriptionResponse>> getAllBySearchParams(
            @RequestParam("codeFiliere")
            String codeFiliere,
            @RequestParam("codeSessionUniversitaire")
            String codeSessionUniversitaire,
            @RequestParam("annee")
            Integer annee,
            @RequestParam("includeEtudiantInfo") boolean includeEtudiantInfo
    );

    @GetExchange(url = "/exists")
    ResponseEntity<Void> existAllByIds(@RequestParam("ids") Set<String> ids);
}
