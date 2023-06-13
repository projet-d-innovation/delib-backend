package ma.enset.moduleservice.service;

import lombok.RequiredArgsConstructor;
import ma.enset.moduleservice.client.ElementClient;
import ma.enset.moduleservice.client.SemestreClient;
import ma.enset.moduleservice.constant.CoreConstants;
import ma.enset.moduleservice.dto.*;
import ma.enset.moduleservice.exception.DuplicateEntryException;
import ma.enset.moduleservice.exception.ElementAlreadyExistsException;
import ma.enset.moduleservice.exception.ElementNotFoundException;
import ma.enset.moduleservice.mapper.ModuleMapper;
import ma.enset.moduleservice.model.Module;
import ma.enset.moduleservice.repository.ModuleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final String ELEMENT_TYPE = "Module";
    private final String ID_FIELD_NAME = "codeModule";
    private final ModuleRepository repository;
    private final ModuleMapper mapper;
    private final ElementClient elementClient;
    private final SemestreClient semestreClient;

    @Override
    public ModuleResponse save(ModuleCreationRequest request) throws ElementAlreadyExistsException {

        semestreClient.semestresExist(Set.of(request.codeSemestre()));

        Module module = mapper.toModule(request);

        try {
            return mapper.toModuleResponse(repository.save(module));
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, request.codeModule()},
                    null
            );
        }
    }

    @Override
    public List<ModuleResponse> saveAll(List<ModuleCreationRequest> request) throws ElementAlreadyExistsException,
            DuplicateEntryException {
        int uniqueModulesCount = (int) request.stream()
                .map(ModuleCreationRequest::codeModule)
                .distinct().count();

        if (uniqueModulesCount != request.size()) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    new Object[]{ELEMENT_TYPE}
            );
        }

        List<Module> foundModules = repository.findAllById(
                request.stream()
                        .map(ModuleCreationRequest::codeModule)
                        .collect(Collectors.toSet())
        );

        if (!foundModules.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE},
                    foundModules.stream()
                            .map(Module::getCodeModule)
                            .toList()
            );
        }

        semestreClient.semestresExist(
                request.stream()
                        .map(ModuleCreationRequest::codeSemestre)
                        .collect(Collectors.toSet())
        );

        List<Module> modules = mapper.toModuleList(request);

        return mapper.toModuleResponseList(repository.saveAll(modules));
    }

    @Override
    public boolean existAllByIds(Set<String> codesModule) throws ElementNotFoundException {

        List<String> foundModulesCodes = repository.findAllById(codesModule)
                .stream().map(Module::getCodeModule).toList();

        if (codesModule.size() != foundModulesCodes.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codesModule.stream()
                            .filter(code -> !foundModulesCodes.contains(code))
                            .toList()
            );
        }

        return true;
    }

    @Override
    public ModuleResponse findById(String codeModule, boolean includeSemestre, boolean includeElements) throws ElementNotFoundException {

        ModuleResponse response = mapper.toModuleResponse(
                repository.findById(codeModule).orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeModule},
                                null
                        )
                )
        );

        if (includeElements) {
            response.setElements(elementClient.getElementsByCodeModule(codeModule).getBody());
        }

        if (includeSemestre && response.getCodeSemestre() != null) {
            response.setSemestre(
                    semestreClient.getById(
                                    response.getCodeSemestre(),
                                    true,
                                    false
                            )
                            .getBody()
            );
        }

        return response;
    }

    @Override
    public List<ModuleResponse> findAllByIds(Set<String> codesModule, boolean includeSemestre, boolean includeElements) throws ElementNotFoundException {

        List<ModuleResponse> response = mapper.toModuleResponseList(repository.findAllById(codesModule));

        if (codesModule.size() != response.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codesModule.stream()
                            .filter(
                                    codeModule -> !response.stream()
                                            .map(ModuleResponse::getCodeModule)
                                            .toList()
                                            .contains(codeModule)
                            )
                            .toList()
            );
        }

        if (includeElements) {
            mapper.enrichModuleResponseListWithElements(
                    response,
                    elementClient.getElementsByCodesModule(codesModule).getBody()
            );
        }
        if (includeSemestre && !response.isEmpty()) {
            Set<String> semestreCodes = response.stream()
                    .map(ModuleResponse::getCodeSemestre)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (!semestreCodes.isEmpty()) {
                List<SemestreResponse> semestres = semestreClient.getAllByIds(
                        semestreCodes,
                        true,
                        false
                ).getBody();
                if (semestres != null && !semestres.isEmpty()) {
                    response.forEach(moduleResponse -> {
                        if (moduleResponse.getCodeSemestre() != null) {
                            moduleResponse.setSemestre(semestres.stream()
                                    .filter(semestreResponse -> semestreResponse.getCodeSemestre().equals(moduleResponse.getCodeSemestre()))
                                    .findFirst().orElse(null));
                        }
                    });
                }
            }

        }

        return response;
    }

    @Override
    public ModulePagingResponse findAll(int page, int size, boolean includeSemestre, boolean includeElements) {

        ModulePagingResponse response = mapper.toPagingResponse(repository.findAll(PageRequest.of(page, size)));

        if (includeElements && !response.records().isEmpty()) {
            Set<String> codesModule = response.records().stream()
                    .map(ModuleResponse::getCodeModule)
                    .collect(Collectors.toSet());

            mapper.enrichModuleResponseListWithElements(
                    response.records(),
                    elementClient.getElementsByCodesModule(codesModule).getBody()
            );
        }
        if (includeSemestre && !response.records().isEmpty()) {
            Set<String> codesSemestre = response.records().stream()
                    .map(ModuleResponse::getCodeSemestre)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (!codesSemestre.isEmpty()) {
                List<SemestreResponse> semestres = semestreClient.getAllByIds(
                        codesSemestre,
                        true,
                        false
                ).getBody();
                if (semestres != null && !semestres.isEmpty()) {
                    response.records().forEach(moduleResponse -> {
                        moduleResponse.setSemestre(
                                semestres.stream()
                                        .filter(
                                                semestreResponse -> semestreResponse.getCodeSemestre()
                                                        .equals(moduleResponse.getCodeSemestre())
                                        ).findFirst()
                                        .orElse(null));
                    });
                }
            }
        }

        return response;
    }

    @Override
    public List<ModuleResponse> findAllByCodeSemestre(String codeSemestre) {
        return mapper.toModuleResponseList(repository.findAllByCodeSemestre(codeSemestre));
    }

    @Override
    public List<GroupedModulesResponse> findAllByCodesSemestre(Set<String> codesSemestre) {
        return repository.findAllByCodeSemestreIn(codesSemestre)
                .stream()
                .collect(Collectors.groupingBy(Module::getCodeSemestre))
                .entrySet().stream()
                .map(entry ->
                        GroupedModulesResponse.builder()
                                .codeSemestre(entry.getKey())
                                .modules(mapper.toModuleResponseList(entry.getValue()))
                                .build()
                )
                .toList();
    }

    @Override
    public ModuleResponse update(String codeModule, ModuleUpdateRequest request) throws ElementNotFoundException {

        Module module = repository.findById(codeModule).orElseThrow(() ->
                new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeModule},
                        null
                )
        );

        mapper.updateModuleFromDTO(request, module);

        return mapper.toModuleResponse(repository.save(module));
    }

    @Override
    @Transactional
    public void deleteById(String codeModule) throws ElementNotFoundException {

        if (!repository.existsById(codeModule)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeModule},
                    null
            );
        }

        repository.deleteById(codeModule);

        elementClient.deleteElementsByCodeModule(codeModule);
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<String> codesModule) throws ElementNotFoundException {
        existAllByIds(codesModule);

        repository.deleteAllById(codesModule);

        elementClient.deleteElementsByCodesModule(codesModule);
    }

    @Override
    @Transactional
    public void deleteAllByCodeSemestre(String codeSemestre) {

        Set<String> modulesToDeleteCodes = repository.findAllByCodeSemestre(codeSemestre)
                .stream()
                .map(Module::getCodeModule)
                .collect(Collectors.toSet());

        if (modulesToDeleteCodes.isEmpty()) {
            return;
        }

        repository.deleteAllByCodeSemestre(codeSemestre);

        elementClient.deleteElementsByCodesModule(modulesToDeleteCodes);
    }

    @Override
    @Transactional
    public void deleteAllByCodesSemestre(Set<String> codesSemestre) {

        Set<String> modulesToDeleteCodes = repository.findAllByCodeSemestreIn(codesSemestre)
                .stream()
                .map(Module::getCodeModule)
                .collect(Collectors.toSet());

        if (modulesToDeleteCodes.isEmpty()) {
            return;
        }

        repository.deleteAllByCodeSemestreIn(codesSemestre);

        elementClient.deleteElementsByCodesModule(modulesToDeleteCodes);
    }
}
