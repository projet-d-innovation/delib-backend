package ma.enset.moduleservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.moduleservice.dto.ModuleCreationRequest;
import ma.enset.moduleservice.dto.ModulePagingResponse;
import ma.enset.moduleservice.dto.ModuleResponse;
import ma.enset.moduleservice.dto.ModuleUpdateRequest;
import ma.enset.moduleservice.model.Module;
import ma.enset.moduleservice.repository.ModuleRepository;
import ma.enset.moduleservice.service.ModuleService;
import ma.enset.moduleservice.util.ModuleMapper;
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
@RequestMapping("/api/v1/modules")
@AllArgsConstructor
@Validated
public class ModuleController {
    private final ModuleService moduleService;
    private final ModuleMapper moduleMapper;
    @PostMapping
    public ResponseEntity<ModuleResponse> save(@Valid @RequestBody ModuleCreationRequest moduleCreationRequest) {
        Module module = moduleMapper.toModule(moduleCreationRequest);
        ModuleResponse moduleResponse = moduleMapper.toModuleResponse(moduleService.save(module));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moduleResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ModuleResponse>> saveAll(@RequestBody List<@Valid ModuleCreationRequest> moduleCreationRequestList) {
        List<Module> moduleList = moduleMapper.toModuleList(moduleCreationRequestList);
        List<ModuleResponse> moduleResponseList = moduleMapper.toModuleResponseList(moduleService.saveAll(moduleList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moduleResponseList);
    }

    @GetMapping("/{codeModule}")
    public ResponseEntity<ModuleResponse> get(@PathVariable("codeModule") String codeModule) {

        Module foundModule = moduleService.findByCodeModule(codeModule);
        ModuleResponse foundModuleResponse = moduleMapper.toModuleResponse(foundModule);

        return ResponseEntity
                .ok()
                .body(foundModuleResponse);
    }

    @GetMapping
    public ResponseEntity<ModulePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                       @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Module> modulePage = moduleService.findAll(pageRequest);
        ModulePagingResponse pagedResponse = moduleMapper.toPagingResponse(modulePage);

        return ResponseEntity
                .ok()
                .body(pagedResponse);
    }

    @PatchMapping("/{codeModule}")
    public ResponseEntity<ModuleResponse> update(
        @PathVariable("codeModule") String codeModule,
        @Valid @RequestBody ModuleUpdateRequest moduleUpdateRequest
    ) {

        Module module = moduleService.findByCodeModule(codeModule);
        moduleMapper.updateModuleFromDTO(moduleUpdateRequest, module);

        Module updatedModule = moduleService.update(module);
        ModuleResponse updatedModuleResponse = moduleMapper.toModuleResponse(updatedModule);

        return ResponseEntity
                .ok()
                .body(updatedModuleResponse);
    }

    @DeleteMapping("/{codeModule}")
    public ResponseEntity<?> delete(@PathVariable("codeModule") String codeModule) {
        moduleService.deleteByCodeModule(codeModule);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestBody List<String> codeModuleList) {
        moduleService.deleteAllByCodeModule(codeModuleList);

        return ResponseEntity
                .noContent()
                .build();
    }
}
