package ma.enset.element.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.element.dto.*;
import ma.enset.element.model.Element;
import ma.enset.element.service.ElementService;
import ma.enset.element.util.ElementMapper;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/elements")
@AllArgsConstructor
@Validated
public class ElementController {
    private final ElementService elementService;
    private final ElementMapper elementMapper;


    @PostMapping
    public ResponseEntity<ElementResponse> save(@Valid @RequestBody ElementCreationRequest elementCreationRequest) {
        Element element = elementMapper.toElement(elementCreationRequest);
        ElementResponse elementResponse = elementMapper.toElementResponse(elementService.save(element));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(elementResponse);

    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ElementResponse>> saveAll(@Valid @RequestBody List<ElementCreationRequest> elementRequests) {
        List<Element> elements = elementMapper.toElementList(elementRequests);
        List<ElementResponse> elementResponses = elementMapper.toElementResponseList(elementService.saveAll(elements));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(elementResponses);
    }

    @PatchMapping("/{codeElement}")
    public ResponseEntity<ElementResponse> update(
            @PathVariable("codeElement") String codeElement,
            @Valid @RequestBody ElementUpdateRequest elementUpdateRequest) {

        Element element = elementService.findByCodeElement(codeElement);
        elementMapper.updateElementFromDTO(elementUpdateRequest, element);
        System.out.println(elementUpdateRequest);
        System.out.println(element);
        Element updatedElement = elementService.update(element);
        ElementResponse elementResponse = elementMapper.toElementResponse(updatedElement);

        return ResponseEntity
                .ok()
                .body(elementResponse);
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<ElementResponse>> updateAll(@Valid @RequestBody List<ElementUpdateRequest> elementUpdateRequests) {

        List<Element> elements = elementService.findAllByCodeElement(elementMapper.toCodeElementList(elementUpdateRequests));

        elementMapper.updateElementFromDTO(elementUpdateRequests, elements);

        List<Element> updatedElements = elementService.updateAll(elements);

        List<ElementResponse> elementResponses = elementMapper.toElementResponseList(updatedElements);

        return ResponseEntity
                .ok()
                .body(elementResponses);
    }


    @DeleteMapping("/{codeElement}")
    public ResponseEntity<Void> delete(
            @PathVariable("codeElement") String codeElement) {
        elementService.deleteByCodeElement(codeElement);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(@RequestParam List<String> codesElement) {
        elementService.deleteAllByCodeElement(codesElement);
        return ResponseEntity
                .noContent()
                .build();
    }


    @GetMapping
    public ResponseEntity<ElementPagingResponse> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Element> elementsPage = elementService.findAll(pageRequest);
        ElementPagingResponse elementPagingResponse = elementMapper.toPagingResponse(elementsPage);

        return ResponseEntity
                .ok()
                .body(elementPagingResponse);
    }

    @GetMapping("{codeElement}")
    public ResponseEntity<ElementResponse> findByCodeElement(@PathVariable("codeElement") String codeElement) {
        Element element = elementService.findByCodeElement(codeElement);
        ElementResponse elementResponse = elementMapper.toElementResponse(element);

        return ResponseEntity
                .ok()
                .body(elementResponse);
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<ElementResponse>> findByCodeElements(@NotEmpty @RequestParam List<String> codesElement) {
        List<Element> elements = elementService.findAllByCodeElement(codesElement);
        List<ElementResponse> elementResponses = elementMapper.toElementResponseList(elements);

        return ResponseEntity
                .ok()
                .body(elementResponses);
    }

    @GetMapping("/module/bulk")
    public ResponseEntity<List<ElementByCodeModuleResponse>> findByModules(@NotEmpty @RequestParam List<String> codesModule) {
        List<List<Element>> elements = elementService.findAllByCodeModule(codesModule);

        List<ElementByCodeModuleResponse> elementByCodeModuleResponses = elementMapper.toElementByCodeModuleResponseList(codesModule,elements);

        return ResponseEntity
                .ok()
                .body(elementByCodeModuleResponses);
    }

    @GetMapping("/module/{codeModule}")
    public ResponseEntity<ElementByCodeModuleResponse> findByModule(@PathVariable("codeModule") String codeModule) {
        List<Element> elements = elementService.findByCodeModule(codeModule);
        ElementByCodeModuleResponse elementByCodeModuleResponse = elementMapper.toElementByCodeModuleResponse(codeModule,elements);

        return ResponseEntity
                .ok()
                .body(elementByCodeModuleResponse);
    }

    @GetMapping("/professeur/bulk")
    public ResponseEntity<List<ElementByCodeProfesseurResponse>> findByProfesseurs(@NotEmpty @RequestParam List<String> codesProfesseur) {
        List<List<Element>> elements = elementService.findAllByCodeProfesseur(codesProfesseur);
        List<ElementByCodeProfesseurResponse> elementByCodeProfesseurResponses = elementMapper.toElementByCodeProfesseurResponseList(codesProfesseur,elements);

        return ResponseEntity
                .ok()
                .body(elementByCodeProfesseurResponses);
    }

    @GetMapping("/professeur/{codeProfesseur}")
    public ResponseEntity<ElementByCodeProfesseurResponse> findByProfesseur(
            @PathVariable("codeProfesseur") String codeProfesseur
    ) {
        List<Element> elements = elementService.findByCodeProfesseur(codeProfesseur);
        ElementByCodeProfesseurResponse elementByCodeProfesseurResponse = elementMapper.toElementByCodeProfesseurResponse(codeProfesseur,elements);

        return ResponseEntity
                .ok()
                .body(elementByCodeProfesseurResponse);
    }


}
