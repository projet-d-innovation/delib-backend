package ma.enset.element.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import ma.enset.element.constant.CoreConstants;
import ma.enset.element.dto.ElementRequestDTO;
import ma.enset.element.dto.ElementResponseDTO;
import ma.enset.element.model.Element;
import ma.enset.element.service.ElementService;
import ma.enset.element.util.ElementMapper;
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
    public ResponseEntity<ElementResponseDTO> create(@Valid @RequestBody ElementRequestDTO elementRequest) {
        Element element = elementMapper.toElement(elementRequest);
        ElementResponseDTO elementResponse = elementMapper.toElementResponse(elementService.create(element));

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<ElementResponseDTO>> createMany(@Valid @RequestBody List<ElementRequestDTO> elementRequests) {
        List<Element> elements = elementMapper.toElements(elementRequests);
        List<ElementResponseDTO> elementResponses = elementMapper.toElementResponses(elementService.createMany(elements));

        return new ResponseEntity<>(
                elementResponses,
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<ElementResponseDTO> update(@Valid @RequestBody ElementRequestDTO elementRequest) {
        Element element = elementMapper.toElement(elementRequest);
        ElementResponseDTO elementResponse = elementMapper.toElementResponse(elementService.update(element));

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.OK
        );
    }

    @PutMapping("/many")
    public ResponseEntity<List<ElementResponseDTO>> updateMany(@Valid @RequestBody List<ElementRequestDTO> elementRequests) {
        List<Element> elements = elementMapper.toElements(elementRequests);
        List<Element> updatedElements = elementService.updateMany(elements);
        List<ElementResponseDTO> elementResponse = elementMapper.toElementResponses(updatedElements);

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.OK
        );
    }


    @DeleteMapping
    public ResponseEntity<Void> delete(@NotBlank @RequestParam String codeElement) {
        elementService.deleteById(codeElement);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(@RequestParam List<String> codesElement) {
        elementService.deleteManyById(codesElement);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping
    public ResponseEntity<Page<ElementResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Element> elementsPage = elementService.findAll(pageRequest);
        Page<ElementResponseDTO> pagedResult = elementsPage.map(elementMapper::toElementResponse);

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @GetMapping("/code")
    public ResponseEntity<ElementResponseDTO> findById(@RequestParam String codeElement) {
        Element element = elementService.findById(codeElement);
        ElementResponseDTO elementResponse = elementMapper.toElementResponse(element);

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.OK
        );
    }

    @GetMapping("/code/many")
    public ResponseEntity<List<ElementResponseDTO>> findByIds(@RequestParam List<String> codesElement) {
        List<Element> elements = elementService.findManyById(codesElement);
        List<ElementResponseDTO> elementResponseDTOS = elements.stream()
                .map(elementMapper::toElementResponse)
                .toList();

        return new ResponseEntity<>(
                elementResponseDTOS,
                HttpStatus.OK
        );
    }

    @GetMapping("/module/many")
    public ResponseEntity<List<List<ElementResponseDTO>>> findByModules(@RequestParam(required = true) List<String> codesModule) {
        List<List<Element>> elements = elementService.findManyByModule(codesModule);
        List<List<ElementResponseDTO>> elementResponseDTOS = elements.stream()
                .map(elementMapper::toElementResponses)
                .toList();

        return new ResponseEntity<>(
                elementResponseDTOS,
                HttpStatus.OK
        );
    }

    @GetMapping("/module")
    public ResponseEntity<List<ElementResponseDTO>> findByModule(@NotBlank @RequestParam(required = true) String codeModule) {
        List<Element> elements = elementService.findByModule(codeModule);
        List<ElementResponseDTO> elementResponseDTOS = elements.stream()
                .map(elementMapper::toElementResponse)
                .toList();

        return new ResponseEntity<>(
                elementResponseDTOS,
                HttpStatus.OK
        );
    }

    @GetMapping("/professeur/many")
    public ResponseEntity<List<List<ElementResponseDTO>>> findByProfesseurs(@RequestParam(required = true) List<String> codesProfesseur) {
        List<List<Element>> elements = elementService.findManyByProfesseur(codesProfesseur);
        List<List<ElementResponseDTO>> elementResponseDTOS = elements.stream()
                .map(elementMapper::toElementResponses)
                .toList();

        return new ResponseEntity<>(
                elementResponseDTOS,
                HttpStatus.OK
        );
    }

    @GetMapping("/professeur")
    public ResponseEntity<List<ElementResponseDTO>> findByProfesseur(@NotBlank @RequestParam(required = true) String codeProfesseur) {
        List<Element> elements = elementService.findByProfesseur(codeProfesseur);
        List<ElementResponseDTO> elementResponseDTOS = elements.stream()
                .map(elementMapper::toElementResponse)
                .toList();

        return new ResponseEntity<>(
                elementResponseDTOS,
                HttpStatus.OK
        );
    }


}
