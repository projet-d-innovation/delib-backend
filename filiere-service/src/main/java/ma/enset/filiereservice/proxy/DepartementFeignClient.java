package ma.enset.filiereservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "departement-service")

public interface DepartementFeignClient {
    String ENDPOINT_PATH = "/api/v1/departements";

    @PutMapping(ENDPOINT_PATH + "/{codeDepartement}/filiere/{codeFiliere}")
    @ResponseStatus(HttpStatus.OK)
    void addFiliereToDepartment(@PathVariable("codeDepartement") String codeDepartement,
                                @PathVariable("codeFiliere") String codeFiliere);

    @DeleteMapping(ENDPOINT_PATH + "/{codeDepartement}/filiere/{codeFiliere}")
    @ResponseStatus(HttpStatus.OK)
    void deleteFiliereFromDepartment(@PathVariable("codeDepartement") String codeDepartement, @PathVariable("codeFiliere") String codeFiliere);

    @GetMapping(ENDPOINT_PATH + "/exist/{codeDepartement}/utilisateurs/{codeUtilisateur}")
    @ResponseStatus(HttpStatus.OK)
    void isUserInDepartment(@PathVariable("codeDepartement") String codeDepartement, @PathVariable("codeUtilisateur") String codeUtilisateur);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ENDPOINT_PATH + "/exist/{codeDepartement}")
    void existsByCodeDepartment(@PathVariable("codeDepartement") String codeDepartement);

}
