package ma.enset.filiereservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "semestre-service")
public interface SemestreFeignClient {
    String ENDPOINT_PATH = "/api/v1/semestres";

    @GetMapping(ENDPOINT_PATH + "/exist/{codeSemestre}")
    @ResponseStatus(HttpStatus.OK)
    boolean doesSemesterExist(@PathVariable("codeSemestre") String codeSemestre);
}
