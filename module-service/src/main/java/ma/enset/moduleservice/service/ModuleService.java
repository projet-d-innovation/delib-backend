package ma.enset.moduleservice.service;

import ma.enset.moduleservice.exception.ElementAlreadyExistsException;
import ma.enset.moduleservice.exception.ElementNotFoundException;
import ma.enset.moduleservice.exception.InternalErrorException;
import ma.enset.moduleservice.model.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModuleService {
    Module save(Module module) throws ElementAlreadyExistsException, InternalErrorException;
    List<Module> saveAll(List<Module> modules) throws ElementAlreadyExistsException, InternalErrorException;
    Module findByCodeModule(String codeModule) throws ElementNotFoundException;
    Page<Module> findAll(Pageable pageable);
    Module update(Module module) throws ElementNotFoundException, InternalErrorException;
    void deleteByCodeModule(String codeModule) throws ElementNotFoundException;
    void deleteAllByCodeModule(List<String> codesModules) throws ElementNotFoundException;
}
