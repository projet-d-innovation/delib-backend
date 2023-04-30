package ma.enset.noteservice.feign;

import ma.enset.noteservice.dto.ElementByCodeModuleResponse;
import ma.enset.noteservice.dto.ModuleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "module-service/api/v1/modules",  configuration = { CustomErrorDecoder.class })
public interface ModuleServiceFeignClient {
    @GetMapping("/{codeModule}")
    ResponseEntity<ModuleResponse> getModuleByCode(@PathVariable("codeModule") String noteModuleId);

    @GetMapping("/semestres/{codeSemestre}")
    ResponseEntity<List<ModuleResponse>> getModuleByCodeSemestre(@PathVariable("codeSemestre") String codeSemestre);
}
