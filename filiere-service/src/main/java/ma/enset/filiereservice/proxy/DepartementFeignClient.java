package ma.enset.filiereservice.proxy;

import ma.enset.filiereservice.dto.DepartementResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( "departement-service")
public interface DepartementFeignClient {
    @GetMapping(value = "/api/v1/departements/code" , consumes = "application/json")
     ResponseEntity<DepartementResponseDTO> findById(@RequestParam String code);
}
