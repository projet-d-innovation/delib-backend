package ma.enset.element.client;


import ma.enset.element.dto.ModuleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url="/api/v1/modules")
public interface ModuleClient {
    @GetExchange(url="{codeModule}")
    ResponseEntity<ModuleResponse> getModule(@PathVariable("codeModule") String codeModule);
}
