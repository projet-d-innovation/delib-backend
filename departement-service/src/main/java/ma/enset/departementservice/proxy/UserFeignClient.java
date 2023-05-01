package ma.enset.departementservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "utilisateur-service")
public interface UserFeignClient {
    String ENDPOINT_PATH = "/api/v1/utilisateurs/professeurs";
    @GetMapping(ENDPOINT_PATH + "/{codeUtilisateur}")
    @ResponseStatus(HttpStatus.OK)
    Object findByCode( @PathVariable("codeUtilisateur") String codeUtilisateur);

}
