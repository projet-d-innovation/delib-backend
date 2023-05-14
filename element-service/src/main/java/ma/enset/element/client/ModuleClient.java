package ma.enset.element.client;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url="/api/v1/modules")
public interface ModuleClient {
    @GetExchange(url="/exists")
    ResponseEntity<Void> modulesExist(@RequestParam("codesModule") Set<String> codesModule);
}
