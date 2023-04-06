package ma.enset.semestreservice.proxy;

import ma.enset.semestreservice.dto.FiliereResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("filiere-service")
public interface FiliereFeignClient {
    @GetMapping(value = "/api/v1/filieres/code", consumes = "application/json")
    public ResponseEntity<FiliereResponseDTO> findById(@RequestParam String code);
}
