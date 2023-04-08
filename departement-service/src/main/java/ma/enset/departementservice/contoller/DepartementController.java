package ma.enset.departementservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import ma.enset.departementservice.constant.CoreConstants;
import ma.enset.departementservice.dto.DepartementRequestDTO;
import ma.enset.departementservice.dto.DepartementResponseDTO;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.service.DepartementServiceImpl;
import ma.enset.departementservice.util.DepartementMapper;
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
@RequestMapping("/api/v1/departements")
public class DepartementController {
    private final DepartementServiceImpl departementServiceImpl;
    private final DepartementMapper departementMapper;


    @PostMapping
    public ResponseEntity<DepartementResponseDTO> create(@Valid @RequestBody DepartementRequestDTO departementRequest) {
        Departement departement = departementMapper.toDepartement(departementRequest);
        DepartementResponseDTO departementResponse = departementMapper.toDepartementResponse(departementServiceImpl.create(departement));

        return new ResponseEntity<>(
                departementResponse,
                HttpStatus.CREATED
        );
    }
    @PostMapping("/many")
    public ResponseEntity<List<DepartementResponseDTO>> createMany(@Valid @RequestBody List<DepartementRequestDTO> departementRequests) {
        List<Departement> departements = departementMapper.toDepartements(departementRequests);
        List<DepartementResponseDTO> departementResponses = departementMapper.toDepartementResponses(departementServiceImpl.createMany(departements));

        return new ResponseEntity<>(
                departementResponses,
                HttpStatus.CREATED
        );
    }
    @PutMapping
    public ResponseEntity<DepartementResponseDTO> update(@Valid @RequestBody DepartementRequestDTO departementRequest) {
        Departement departement = departementMapper.toDepartement(departementRequest);
        DepartementResponseDTO departementResponses = departementMapper.toDepartementResponse(departementServiceImpl.update(departement));

        return new ResponseEntity<>(
                departementResponses,
                HttpStatus.OK
        );
    }
    @PutMapping("/many")
    public ResponseEntity<List<DepartementResponseDTO>> updateMany(@Valid @RequestBody List<DepartementRequestDTO> departementRequests) {
        List<Departement> departements = departementMapper.toDepartements(departementRequests);
        List<DepartementResponseDTO> departementResponses = departementMapper.toDepartementResponses(departementServiceImpl.updateMany(departements));

        return new ResponseEntity<>(
                departementResponses,
                HttpStatus.CREATED
        );
    }
    @DeleteMapping
    public ResponseEntity<Void> delete(@NotBlank @RequestParam String code) {
        departementServiceImpl.deleteById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(@RequestParam List<String> code) {
        departementServiceImpl.deleteManyById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<Page<DepartementResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Departement> departementsPage = departementServiceImpl.findAll(pageRequest);
        Page<DepartementResponseDTO> pagedResult = departementsPage.map(departementMapper::toDepartementResponse);

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }
    @GetMapping("/code")
    public ResponseEntity<DepartementResponseDTO> findById(@RequestParam String code) {
        Departement departement = departementServiceImpl.findById(code);
        DepartementResponseDTO elementResponse = departementMapper.toDepartementResponse(departement);

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.OK
        );
    }
    @GetMapping("/code/many")
    public ResponseEntity<List<DepartementResponseDTO>> findByIds(@RequestParam List<String> codes) {
        List<Departement> departements = departementServiceImpl.findManyById(codes);
        List<DepartementResponseDTO> departementResponseDTOS = departements.stream()
                .map(departementMapper::toDepartementResponse)
                .toList();

        return new ResponseEntity<>(
                departementResponseDTOS,
                HttpStatus.OK
        );
    }






}
