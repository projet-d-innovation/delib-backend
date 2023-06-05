package ma.enset.noteservice.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import ma.enset.noteservice.dto.ModuleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/modules")
public interface ModuleClient {

    @GetExchange(url = "/{codeModule}")
    ResponseEntity<ModuleResponse> getById(
            @PathVariable("codeModule") String codeModule,
            @RequestParam(name = "includeElements", defaultValue = "false") boolean includeElements
    );

    @GetExchange(url = "/bulk")
    ResponseEntity<List<ModuleResponse>> getAllByIds(
            @RequestParam("codesModule") Set<String> codesModule,
            @RequestParam(name = "includeElements", defaultValue = "false") boolean includeElements
    );

    @GetExchange(url = "/exists")
    ResponseEntity<Void> existAllByIds(
            @RequestParam("codesModule") Set<String> codesModule
    );
}
