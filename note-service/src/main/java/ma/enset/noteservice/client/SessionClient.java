package ma.enset.noteservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/session")
public interface SessionClient {
    @GetExchange(url = "/exists")
    ResponseEntity<Void> existsAll(@RequestParam("codesSession") Set<String> codesSession);
}
