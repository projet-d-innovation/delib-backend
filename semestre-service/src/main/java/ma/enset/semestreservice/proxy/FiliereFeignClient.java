package ma.enset.semestreservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "filiere-service")
public interface FiliereFeignClient {
    String ENDPOINT_PATH = "/api/v1/filieres";

    @GetMapping(ENDPOINT_PATH + "/exist/{codeFiliere}")
    @ResponseStatus(HttpStatus.OK)
    void doesFiliereExist(@PathVariable("codeFiliere") String codeFiliere);

    @PutMapping( ENDPOINT_PATH+ "/{codeFiliere}/semestres/{codeSemestre}")
    void addSemestreToFiliere(@PathVariable("codeFiliere") String codeFiliere,
                                @PathVariable("codeSemestre") String codeSemestre);

    @DeleteMapping(ENDPOINT_PATH + "/{codeFiliere}/semestres/{codeSemestre}")
    void removeSemestreFromFiliere(@PathVariable("codeFiliere") String codeFiliere,
                                   @PathVariable("codeSemestre") String codeSemestre);


}
