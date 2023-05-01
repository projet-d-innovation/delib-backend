package ma.enset.departementservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "filiere-service")
public interface FiliereFeignClient {
    String ENDPOINT_PATH = "/api/v1/filieres";

    @GetMapping(ENDPOINT_PATH + "/exist/{codeFiliere}")
    @ResponseStatus(HttpStatus.OK)
    void doesFiliereExist(@PathVariable("codeFiliere") String codeFiliere);


    @GetMapping(ENDPOINT_PATH + "/isChef/{codeUser}")
    boolean isThisUserAChef(@PathVariable("codeUser") String codeUser);
}
