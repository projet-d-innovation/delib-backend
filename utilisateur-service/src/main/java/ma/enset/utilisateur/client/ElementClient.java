//package ma.enset.utilisateur.client;
//
//
//import ma.enset.utilisateur.dto.ElementByCodeProfesseurResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.service.annotation.GetExchange;
//import org.springframework.web.service.annotation.HttpExchange;
//
//import java.util.List;
//
//@HttpExchange(url = "/api/v1/elements")
//public interface ElementClient {
//    @GetExchange(url = "professeur/{codeProfesseur}")
//    ResponseEntity<ElementByCodeProfesseurResponse> getElementsByCodeProfesseur(@PathVariable("codeProfesseur") String codeProfesseur);
//
//    @GetExchange(url = "professeur/bulk")
//    ResponseEntity<List<ElementByCodeProfesseurResponse>> getElementsByCodeProfesseurs(@RequestParam("codesProfesseur") List<String> codesProfesseur);
//}
