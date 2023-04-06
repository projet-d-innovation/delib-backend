package ma.enset.departementservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("filiere-service")
public interface FiliereFeignClient {
    @GetMapping(value = "/api/v1/filieres/departement/code", consumes = "application/json")
    public ResponseEntity<Boolean> existsByCodeDepartement(@RequestParam String code) ;
}
