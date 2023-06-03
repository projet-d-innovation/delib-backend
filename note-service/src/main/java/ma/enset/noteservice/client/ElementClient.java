package ma.enset.noteservice.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import ma.enset.noteservice.dto.ElementResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/elements")
public interface ElementClient {

    @GetExchange(url = "/{codeElement}")
    ResponseEntity<ElementResponse> get(@PathVariable("codeElement") String codeElement);

    @GetExchange(url = "/bulk")
    ResponseEntity<List<ElementResponse>> getAllByIds(
            @RequestParam("codesElement") @NotEmpty Set<@NotBlank String> codesElement
    );

    @GetExchange(url = "/exist")
    ResponseEntity<Void> existAll(
            @RequestParam("codesElement") @NotEmpty Set<@NotBlank String> codesElement
    );
}
