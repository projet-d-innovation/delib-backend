package ma.enset.noteservice.feign;

import ma.enset.noteservice.dto.ModuleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "model-service/api/v1/modules",  configuration = { CustomErrorDecoder.class })
public interface ModuleServiceFeignClient {
    @GetMapping("/{codeModule}")
    ResponseEntity<ModuleResponse> getModuleByCode(@PathVariable("codeModule") String noteModuleId);
}
