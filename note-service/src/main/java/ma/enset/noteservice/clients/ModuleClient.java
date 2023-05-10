package ma.enset.noteservice.clients;

import ma.enset.noteservice.dto.ModuleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url="/api/v1/modules")
public interface ModuleClient {

    @GetExchange("/{codeModule}")
    ResponseEntity<ModuleResponse> getModule(@PathVariable("codeModule") String noteModuleId);

    @GetExchange("/semestres/{codeSemestre}")
    ResponseEntity<List<ModuleResponse>> getModuleByCodeSemestre(@PathVariable("codeSemestre") String codeSemestre);

}
