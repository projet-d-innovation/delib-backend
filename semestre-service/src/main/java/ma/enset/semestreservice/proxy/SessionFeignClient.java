package ma.enset.semestreservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "session-service")
public interface SessionFeignClient {
    String ENDPOINT_PATH = "/api/v1/sessions";
    @GetMapping(ENDPOINT_PATH + "/{codeSession}")
    boolean findByCode(@PathVariable("codeSession") String codeSession);
}
