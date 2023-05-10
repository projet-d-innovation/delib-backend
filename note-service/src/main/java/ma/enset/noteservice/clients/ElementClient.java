package ma.enset.noteservice.clients;

import jakarta.validation.constraints.NotEmpty;
import ma.enset.noteservice.dto.ElementByCodeModuleResponse;
import ma.enset.noteservice.dto.ElementResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url="/api/v1/elements")
public interface ElementClient {
    @GetExchange(url="{codeElement}")
    ResponseEntity<ElementResponse> getElement(@PathVariable("codeElement") String codeElement);
    @GetExchange("/bulk")
    ResponseEntity<List<ElementResponse>> getElements(@NotEmpty @RequestParam("codesElement") List<String> codesElement);

    @GetExchange("/module/{codeModule}")
    ResponseEntity<ElementByCodeModuleResponse> getElementByModule(@PathVariable("codeModule") String codeModule);

}
