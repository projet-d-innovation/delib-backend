package ma.enset.filiereservice.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import ma.enset.filiereservice.dto.UtilisateurResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/utilisateurs")
public interface UtilisateurClient {
    @GetExchange(url = "/exists")
    ResponseEntity<Void> existsById(
            @RequestParam("codes") Set<String> codes
    );

    @GetExchange(url = "/{code}")
    ResponseEntity<UtilisateurResponse> findById(
            @PathVariable("code") String code
    );

    @GetExchange(url = "/bulk")
    ResponseEntity<List<UtilisateurResponse>> findAllById(
            @RequestParam("codes") Set<String> codes
    );

}
