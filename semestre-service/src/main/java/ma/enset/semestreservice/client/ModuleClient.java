package ma.enset.semestreservice.client;


import ma.enset.semestreservice.dto.SemestreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api/v1/modules/semestre")
public interface ModuleClient {
    @GetExchange(url = "/{codeSemestre}")
    ResponseEntity<List<SemestreResponse.Module>> getModulesByCodeSemestre(@PathVariable("codeSemestre") String codeSemestre);

    @GetExchange(url = "/bulk")
    ResponseEntity<List<SemestreResponse>> getModulesByCodesSemestre(@RequestParam("codesSemestre") Set<String> codesSemestre);

    @DeleteExchange(url = "/{codeSemestre}")
    void deleteModulesByCodeSemestre(@PathVariable("codeSemestre") String codeSemestre);

    @DeleteExchange(url = "/bulk")
    void deleteModulesByCodesSemestre(@RequestBody Set<String> codesSemestre);
}
