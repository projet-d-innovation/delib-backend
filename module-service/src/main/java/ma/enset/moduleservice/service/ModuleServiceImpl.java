package ma.enset.moduleservice.service;

import lombok.RequiredArgsConstructor;
import ma.enset.moduleservice.constant.CoreConstants;
import ma.enset.moduleservice.dto.ModuleCreationRequest;
import ma.enset.moduleservice.dto.ModulePagingResponse;
import ma.enset.moduleservice.dto.ModuleResponse;
import ma.enset.moduleservice.dto.ModuleUpdateRequest;
import ma.enset.moduleservice.exception.DuplicateEntryException;
import ma.enset.moduleservice.exception.ElementAlreadyExistsException;
import ma.enset.moduleservice.exception.ElementNotFoundException;
import ma.enset.moduleservice.mapper.ModuleMapper;
import ma.enset.moduleservice.model.Module;
import ma.enset.moduleservice.repository.ModuleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final String ELEMENT_TYPE = "Module";
    private final String ID_FIELD_NAME = "codeModule";
    private final ModuleRepository repository;
    private final ModuleMapper mapper;

    @Override
    public ModuleResponse save(ModuleCreationRequest request) throws ElementAlreadyExistsException {

        // TODO (aymane): check the existence of the semester before saving

        Module module = mapper.toModule(request);

        try {
            return mapper.toModuleResponse(repository.save(module));
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, request.codeModule()},
                null
            );
        }
    }

    @Override
    public List<ModuleResponse> saveAll(List<ModuleCreationRequest> request) throws ElementAlreadyExistsException,
                                                                                    DuplicateEntryException {
        List<Module> foundModules = repository.findAllById(
            request.stream()
                    .map(ModuleCreationRequest::codeModule)
                    .collect(Collectors.toSet())
        );

        if (!foundModules.isEmpty()) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                new Object[] {ELEMENT_TYPE},
                foundModules.stream()
                            .map(Module::getCodeModule)
                            .toList()
            );
        }

        // TODO (aymane): check the existence of the semesters before saving

        List<Module> modules = mapper.toModuleList(request);

        try {
            return mapper.toModuleResponseList(repository.saveAll(modules));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException(
                CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                null
            );
        }
    }

    @Override
    public ModuleResponse findById(String codeModule, boolean includeElements) throws ElementNotFoundException {

        ModuleResponse response = mapper.toModuleResponse(
            repository.findById(codeModule).orElseThrow(() ->
                new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, codeModule},
                    null
                )
            )
        );

//        if (includeElements) {
//            // TODO (aymane): fetch the module's elements from the `Element` service
//            //                and inject them in the response
//        }

        return response;
    }

    @Override
    public ModulePagingResponse findAll(int page, int size, boolean includeElements) {

        ModulePagingResponse response = mapper.toPagingResponse(
            repository.findAll(PageRequest.of(page, size))
        );

//        if (includeElements) {
//            // TODO (aymane): fetch the modules' elements from the `Element` service
//            //                and inject them in the response
//        }

        return response;
    }

    @Override
    public boolean existsAllId(Set<String> codesModule) throws ElementNotFoundException {

        List<String> foundModulesCodes = repository.findAllById(codesModule)
                                                    .stream().map(Module::getCodeModule).toList();

        if (codesModule.size() != foundModulesCodes.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                codesModule.stream()
                            .filter(code -> !foundModulesCodes.contains(code))
                            .toList()
            );
        }

        return true;
    }

    @Override
    public ModuleResponse update(String codeModule, ModuleUpdateRequest request) throws ElementNotFoundException {

        Module module = repository.findById(codeModule).orElseThrow(() ->
            new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, codeModule},
                null
            )
        );

        mapper.updateModuleFromDTO(request, module);

        return mapper.toModuleResponse(repository.save(module));
    }

    @Override
    public void deleteById(String codeModule) throws ElementNotFoundException {

        if (!repository.existsById(codeModule)) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, codeModule},
                null
            );
        }

        repository.deleteById(codeModule);

        // TODO (aymane): trigger the deletion of the `Elements`
        //                that are linked to the `codeModule`
    }

    @Override
    public void deleteByCodeSemestre(String codeSemestre) {
        repository.deleteAllByCodeSemestre(codeSemestre);
    }

    @Override
    public void deleteAllById(Set<String> codesModule) throws ElementNotFoundException {

        existsAllId(codesModule);

        repository.deleteAllById(codesModule);

        // TODO (aymane): trigger the deletion of the `Elements`
        //                that are linked to the `codesModule`
    }

}
