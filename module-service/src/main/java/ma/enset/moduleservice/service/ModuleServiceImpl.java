package ma.enset.moduleservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.moduleservice.constant.CoreConstants;
import ma.enset.moduleservice.exception.ElementAlreadyExistsException;
import ma.enset.moduleservice.exception.ElementNotFoundException;
import ma.enset.moduleservice.exception.InternalErrorException;
import ma.enset.moduleservice.model.Module;
import ma.enset.moduleservice.repository.ModuleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;

    @Override
    public Module save(Module module) throws ElementAlreadyExistsException, InternalErrorException {
        if (moduleRepository.existsByCodeModule(module.getCodeModule())) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.MODULE_ALREADY_EXISTS)
                    .args(new Object[]{module.getCodeModule()})
                    .build();
        }

        Module createdModule = null;

        try {
            createdModule = moduleRepository.save(module);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return createdModule;
    }

    @Override
    @Transactional
    public List<Module> saveAll(List<Module> modules) throws ElementAlreadyExistsException, InternalErrorException {
        List<Module> createdModules = new ArrayList<>(modules.size());

        modules.forEach(module -> createdModules.add(save(module)));

        return createdModules;
    }

    @Override
    public Module findByCodeModule(String codeModule) throws ElementNotFoundException {
        return moduleRepository.findByCodeModule(codeModule)
                    .orElseThrow(() -> moduleNotFoundException(codeModule));
    }

    @Override
    public Page<Module> findAll(Pageable pageable) {
        return moduleRepository.findAll(pageable);
    }

    @Override
    public Module update(Module module) throws ElementNotFoundException, InternalErrorException {
        if (!moduleRepository.existsByCodeModule(module.getCodeModule())) {
            throw moduleNotFoundException(module.getCodeModule());
        }

        Module updatedModule = null;

        try {
            updatedModule = moduleRepository.save(module);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedModule;
    }

    @Override
    public void deleteByCodeModule(String codeModule) throws ElementNotFoundException {
        if (!moduleRepository.existsByCodeModule(codeModule)) {
            throw moduleNotFoundException(codeModule);
        }

        moduleRepository.deleteByCodeModule(codeModule);
    }

    @Override
    @Transactional
    public void deleteAllByCodeModule(List<String> codesModules) throws ElementNotFoundException {
        codesModules.forEach(this::deleteByCodeModule);
    }

    private ElementNotFoundException moduleNotFoundException(String codeModule) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.MODULE_NOT_FOUND)
                .args(new Object[]{codeModule})
                .build();
    }

}
