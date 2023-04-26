package ma.enset.noteservice.feign;

import jakarta.validation.constraints.NotEmpty;
import ma.enset.noteservice.dto.ElementByCodeModuleResponse;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.dto.ModuleResponse;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@LoadBalancerClients
@FeignClient(name = "ELEMENT-SERVICE/api/v1/elements", configuration = { CustomErrorDecoder.class })
public interface ElementServiceFeignClient {
    @GetMapping("/{codeElement}")
    ResponseEntity<ElementResponse> getElementByCode(@PathVariable("codeElement") String codeElement);

    @GetMapping("/bulk")
    ResponseEntity<List<ElementResponse>> findByCodeElements11(@NotEmpty @RequestParam("codesElement") List<String> codesElement);
    @GetMapping("/module/{codeModule}")
    ResponseEntity<ElementByCodeModuleResponse> findByModule(@PathVariable("codeModule") String codeModule);
}

