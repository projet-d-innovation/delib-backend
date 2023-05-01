package ma.enset.filiereservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "annee-universitaire-service")
public interface AnnUnivFeignClient {
    String ENDPOINT_PATH = "/api/v1/annee-universitaires";

    @GetMapping(ENDPOINT_PATH + "/exist/{codeAnnUniv}")
    @ResponseStatus(HttpStatus.OK)
    boolean doesAnnUnivExist(@PathVariable("codeAnnUniv") String codeAnnUniv);
}
