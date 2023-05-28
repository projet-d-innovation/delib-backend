package ma.enset.moduleservice.client;

import ma.enset.moduleservice.dto.ModuleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/elements/module")
public interface ElementClient {
    @GetExchange(url = "/{codeModule}")
    ResponseEntity<List<ModuleResponse.Element>> getElementsByCodeModule(@PathVariable("codeModule") String codeModule);

    @GetExchange(url = "/bulk")
    ResponseEntity<List<ModuleResponse>> getElementsByCodesModule(@RequestParam Set<String> codesModule);

    @DeleteExchange(url = "/{codeModule}")
    ResponseEntity<Void> deleteElementsByCodeModule(@PathVariable("codeModule") String codeModule);

    @DeleteExchange(url = "/bulk")
    ResponseEntity<Void> deleteElementsByCodesModule(@RequestBody Set<String> codesModule);
}
