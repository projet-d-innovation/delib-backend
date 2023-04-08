package ma.enset.filiereservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.dto.RegleDeCalculRequestDTO;
import ma.enset.filiereservice.dto.RegleDeCalculResponseDTO;
import ma.enset.filiereservice.model.RegleDeCalcul;
import ma.enset.filiereservice.service.RegleDeCalculServiceImpl;
import ma.enset.filiereservice.util.RegleDeCalculMapper;
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
@RequestMapping("/api/v1/regledecalcul")
public class RegleDeCalculController {
    private final RegleDeCalculServiceImpl regleDeCalculServiceImpl;
    private final RegleDeCalculMapper regleDeCalculMapper;

    @PostMapping
    public ResponseEntity<RegleDeCalculResponseDTO> create(@Valid @RequestBody RegleDeCalculRequestDTO regleDeCalculRequest) {
        RegleDeCalcul regleDeCalcul = regleDeCalculMapper.toRegleDeCalcul(regleDeCalculRequest);
        RegleDeCalculResponseDTO regleDeCalculResponse = regleDeCalculMapper.toRegleDeCalculResponse(regleDeCalculServiceImpl.create(regleDeCalcul));

        return new ResponseEntity<>(
                regleDeCalculResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<RegleDeCalculResponseDTO>> createMany(@Valid @RequestBody List<RegleDeCalculRequestDTO> regleDeCalculRequests) {
        List<RegleDeCalcul> regleDeCalculs = regleDeCalculMapper.toRegleDeCalculs(regleDeCalculRequests);
        List<RegleDeCalculResponseDTO> regleDeCalculResponses = regleDeCalculMapper.toRegleDeCalculResponses(regleDeCalculServiceImpl.createMany(regleDeCalculs));

        return new ResponseEntity<>(
                regleDeCalculResponses,
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<RegleDeCalculResponseDTO> update(@Valid @RequestBody RegleDeCalculRequestDTO regleDeCalculRequest) {
        RegleDeCalcul regleDeCalcul = regleDeCalculMapper.toRegleDeCalcul(regleDeCalculRequest);
        RegleDeCalculResponseDTO fregleDeCalculResponses = regleDeCalculMapper.toRegleDeCalculResponse(regleDeCalculServiceImpl.update(regleDeCalcul));

        return new ResponseEntity<>(
                fregleDeCalculResponses,
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@NotBlank @RequestParam String code) {
        regleDeCalculServiceImpl.deleteById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(@RequestParam List<String> code) {
        regleDeCalculServiceImpl.deleteManyById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<RegleDeCalculResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<RegleDeCalcul> regleDeCalculsPage = regleDeCalculServiceImpl.findAll(pageRequest);
        Page<RegleDeCalculResponseDTO> pagedResult = regleDeCalculsPage.map(regleDeCalculMapper::toRegleDeCalculResponse);

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @GetMapping("/code")
    public ResponseEntity<RegleDeCalculResponseDTO> findById(@RequestParam String code) {
        RegleDeCalcul regleDeCalcul = regleDeCalculServiceImpl.findById(code);
        RegleDeCalculResponseDTO elementResponse = regleDeCalculMapper.toRegleDeCalculResponse(regleDeCalcul);

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.OK
        );
    }

    @GetMapping("/code/many")
    public ResponseEntity<List<RegleDeCalculResponseDTO>> findByIds(@RequestParam List<String> codes) {
        List<RegleDeCalcul> regleDeCalculs = regleDeCalculServiceImpl.findManyById(codes);
        List<RegleDeCalculResponseDTO> regleDeCalculResponseDTOS = regleDeCalculs.stream()
                .map(regleDeCalculMapper::toRegleDeCalculResponse)
                .toList();

        return new ResponseEntity<>(
                regleDeCalculResponseDTOS,
                HttpStatus.OK
        );
    }



}
