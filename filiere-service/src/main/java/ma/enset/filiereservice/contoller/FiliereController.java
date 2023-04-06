package ma.enset.filiereservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.dto.FiliereRequestDTO;
import ma.enset.filiereservice.dto.FiliereResponseDTO;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.service.FiliereService;
import ma.enset.filiereservice.util.FiliereMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/filieres")
public class FiliereController {
    private final FiliereService filiereService;
    private final FiliereMapper filiereMapper;


    @PostMapping
    public ResponseEntity<FiliereResponseDTO> create(@Valid @RequestBody FiliereRequestDTO filiereRequest) {
        Filiere filiere = filiereMapper.toFiliere(filiereRequest);
        FiliereResponseDTO filiereResponse = filiereMapper.toFiliereResponse(filiereService.create(filiere));

        return new ResponseEntity<>(
                filiereResponse,
                HttpStatus.CREATED
        );
    }
    @PostMapping("/many")
    public ResponseEntity<List<FiliereResponseDTO>> createMany(@Valid @RequestBody List<FiliereRequestDTO> filiereRequests) {
        List<Filiere> filieres = filiereMapper.toFilieres(filiereRequests);
        List<FiliereResponseDTO> filiereResponses = filiereMapper.toFiliereResponses(filiereService.createMany(filieres));

        return new ResponseEntity<>(
                filiereResponses,
                HttpStatus.CREATED
        );
    }
    @PutMapping
    public ResponseEntity<FiliereResponseDTO> update(@Valid @RequestBody FiliereRequestDTO filiereRequest) {
        Filiere filiere = filiereMapper.toFiliere(filiereRequest);
        FiliereResponseDTO filiereResponses = filiereMapper.toFiliereResponse(filiereService.update(filiere));

        return new ResponseEntity<>(
                filiereResponses,
                HttpStatus.OK
        );
    }
    @DeleteMapping
    public ResponseEntity<Void> delete(@NotBlank @RequestParam String code) {
        filiereService.deleteById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(@RequestParam List<String> code) {
        filiereService.deleteManyById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<Page<FiliereResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Filiere> filieresPage = filiereService.findAll(pageRequest);
        Page<FiliereResponseDTO> pagedResult = filieresPage.map(filiereMapper::toFiliereResponse);

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }
    @GetMapping("/code")
    public ResponseEntity<FiliereResponseDTO> findById(@RequestParam String code) {
        Filiere filiere = filiereService.findById(code);
        FiliereResponseDTO elementResponse = filiereMapper.toFiliereResponse(filiere);

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.OK
        );
    }
    @GetMapping("/code/many")
    public ResponseEntity<List<FiliereResponseDTO>> findByIds(@RequestParam List<String> codes) {
        List<Filiere> filieres = filiereService.findManyById(codes);
        List<FiliereResponseDTO> filiereResponseDTOS = filieres.stream()
                .map(filiereMapper::toFiliereResponse)
                .toList();

        return new ResponseEntity<>(
                filiereResponseDTOS,
                HttpStatus.OK
        );
    }

    @GetMapping("/departement/code")
    public ResponseEntity<Boolean> existsByCodeDepartement(@RequestParam String code) {
        Boolean exists = filiereService.existsByCodeDepartement(code);

        return new ResponseEntity<>(
                exists,
                HttpStatus.OK
        );
    }





}
