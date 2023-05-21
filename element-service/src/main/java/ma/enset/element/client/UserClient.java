package ma.enset.element.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/utilisateurs")
public interface UserClient {
    @GetExchange(url="/{code}")
    ResponseEntity<Void> profExits(@PathVariable("code") String code);

    @GetExchange(url="/bulk")
    ResponseEntity<Void> allProfsExit(@RequestParam("codes") Set<String> codes);
}
