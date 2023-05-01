package ma.enset.semestreservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "module-service")
public interface ModuleFeignClient {
    String ENDPOINT_PATH = "/api/v1/modules";

    @GetMapping(ENDPOINT_PATH + "/{codeModule}")
    Object findByCode(@PathVariable("codeModule") String codeModule);

}
