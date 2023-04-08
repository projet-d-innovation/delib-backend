package ma.enset.filiereservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("semestre-service")
public interface SemestreFeignClient {

    @GetMapping(value = "/api/v1/semestres/code", consumes = "application/json")
    public ResponseEntity<Boolean> existsById(@RequestParam String code) ;
}
