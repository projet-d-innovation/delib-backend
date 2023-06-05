package ma.enset.noteservice.client;

import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.dto.GroupedElementsResponse;
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
            @RequestParam("codesElement") Set<String> codesElement
    );

    @GetExchange(url = "/exist")
    ResponseEntity<Void> existAll(
            @RequestParam("codesElement") Set<String> codesElement
    );

    @GetExchange(url = "/module/{codeModule}")
    ResponseEntity<List<ElementResponse>> getAllByCodeModule(@PathVariable("codeModule") String codeModule);

    @GetExchange(url = "/module/bulk")
    ResponseEntity<List<GroupedElementsResponse>> getAllByCodesModule(
            @RequestParam("codesModule") Set<String> codesModule
    );

}
