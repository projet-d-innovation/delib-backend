package ma.enset.semestreservice.service;

import lombok.RequiredArgsConstructor;
import ma.enset.semestreservice.client.FiliereClient;
import ma.enset.semestreservice.client.ModuleClient;
import ma.enset.semestreservice.constant.CoreConstants;
import ma.enset.semestreservice.dto.*;
import ma.enset.semestreservice.exception.DuplicateEntryException;
import ma.enset.semestreservice.exception.ElementAlreadyExistsException;
import ma.enset.semestreservice.exception.ElementNotFoundException;
import ma.enset.semestreservice.mapper.SemestreMapper;
import ma.enset.semestreservice.model.Semestre;
import ma.enset.semestreservice.repository.SemestreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemestreServiceImpl implements SemestreService {
    private final String ELEMENT_TYPE = "Semestre";
    private final String ID_FIELD_NAME = "codeSemestre";
    private final SemestreRepository repository;
    private final SemestreMapper mapper;
    private final FiliereClient filiereClient;
    private final ModuleClient moduleClient;

    @Override
    public SemestreResponse save(SemestreCreationRequest request) throws ElementAlreadyExistsException {

        filiereClient.getFiliereById(request.codeFiliere());

        Semestre semestre = mapper.toSemestre(request);

        try {
            return mapper.toSemestreResponse(repository.save(semestre));
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, request.codeSemestre()},
                    null
            );
        }
    }

    @Override
    public List<SemestreResponse> saveAll(List<SemestreCreationRequest> request) throws ElementAlreadyExistsException,
            DuplicateEntryException {
        int uniqueSemestresCount = (int) request.stream()
                .map(SemestreCreationRequest::codeSemestre)
                .distinct().count();

        if (uniqueSemestresCount != request.size()) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    new Object[]{ELEMENT_TYPE}
            );
        }

        List<Semestre> foundSemestres = repository.findAllById(
                request.stream()
                        .map(SemestreCreationRequest::codeSemestre)
                        .collect(Collectors.toSet())
        );

        if (!foundSemestres.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE},
                    foundSemestres.stream()
                            .map(Semestre::getCodeSemestre)
                            .toList()
            );
        }

        filiereClient.getAllFilieresByIds(
                request.stream()
                        .map(SemestreCreationRequest::codeFiliere)
                        .collect(Collectors.toSet())
        );

        List<Semestre> semestres = mapper.toSemestreList(request);

        return mapper.toSemestreResponseList(repository.saveAll(semestres));
    }

    @Override
    public boolean existAllByIds(Set<String> codesSemestre) throws ElementNotFoundException {

        List<String> foundSemestresCodes = repository.findAllById(codesSemestre)
                .stream()
                .map(Semestre::getCodeSemestre).toList();

        if (codesSemestre.size() != foundSemestresCodes.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codesSemestre.stream()
                            .filter(code -> !foundSemestresCodes.contains(code))
                            .toList()
            );
        }

        return true;
    }

    @Override
    public SemestreResponse findById(String codeSemestre, boolean includeModules) throws ElementNotFoundException {

        SemestreResponse response = mapper.toSemestreResponse(
                repository.findById(codeSemestre).orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeSemestre},
                                null
                        )
                )
        );

        if (includeModules) {
            response.setModules(moduleClient.getModulesByCodeSemestre(codeSemestre).getBody());
        }

        return response;
    }

    @Override
    public List<SemestreResponse> findAllByIds(Set<String> codesSemestre, boolean includeModules) throws ElementNotFoundException {

        List<SemestreResponse> response = mapper.toSemestreResponseList(repository.findAllById(codesSemestre));

        if (codesSemestre.size() != response.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codesSemestre.stream()
                            .filter(
                                    codeSemestre -> !response.stream()
                                            .map(SemestreResponse::getCodeSemestre)
                                            .toList()
                                            .contains(codeSemestre)
                            )
                            .toList()
            );
        }

        if (includeModules && !codesSemestre.isEmpty()) {
            mapper.enrichSemestreResponseListWithModuless(
                    response,
                    moduleClient.getModulesByCodesSemestre(codesSemestre).getBody()
            );
        }

        return response;
    }

    @Override
    public SemestrePagingResponse findAll(int page, int size, boolean includeModules) {

        SemestrePagingResponse response = mapper.toPagingResponse(repository.findAll(PageRequest.of(page, size)));

        if (includeModules && !response.records().isEmpty()) {
            Set<String> codesSemestre = response.records().stream()
                    .map(SemestreResponse::getCodeSemestre)
                    .collect(Collectors.toSet());

            mapper.enrichSemestreResponseListWithModuless(
                    response.records(),
                    moduleClient.getModulesByCodesSemestre(codesSemestre).getBody()
            );
        }

        return response;
    }

    @Override
    public List<SemestreResponse> findAllByCodeFiliere(String codeFiliere) {
        return mapper.toSemestreResponseList(repository.findAllByCodeFiliere(codeFiliere));
    }

    @Override
    public List<GroupedSemestresResponse> findAllByCodesFiliere(Set<String> codesFiliere) {
        return repository.findAllByCodeFiliereIn(codesFiliere)
                .stream()
                .collect(Collectors.groupingBy(Semestre::getCodeFiliere))
                .entrySet().stream()
                .map(entry ->
                        GroupedSemestresResponse.builder()
                                .codeFiliere(entry.getKey())
                                .semestres(mapper.toSemestreResponseList(entry.getValue()))
                                .build()
                )
                .toList();
    }

    @Override
    public SemestreResponse update(String codeSemestre, SemestreUpdateRequest request) throws ElementNotFoundException {

        Semestre semestre = repository.findById(codeSemestre).orElseThrow(() ->
                new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeSemestre},
                        null
                )
        );

        mapper.updateSemestreFromDTO(request, semestre);

        return mapper.toSemestreResponse(repository.save(semestre));
    }

    @Override
    @Transactional
    public void deleteById(String codeSemestre) throws ElementNotFoundException {

        if (!repository.existsById(codeSemestre)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeSemestre},
                    null
            );
        }

        repository.deleteById(codeSemestre);

        moduleClient.deleteModulesByCodeSemestre(codeSemestre);
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<String> codesSemestre) throws ElementNotFoundException {
        existAllByIds(codesSemestre);

        repository.deleteAllById(codesSemestre);

        moduleClient.deleteModulesByCodesSemestre(codesSemestre);
    }

    @Override
    @Transactional
    public void deleteAllByCodeFiliere(String codeFiliere) {

        Set<String> semestresToDeleteCodes = repository.findAllByCodeFiliere(codeFiliere)
                .stream()
                .map(Semestre::getCodeSemestre)
                .collect(Collectors.toSet());

        if (semestresToDeleteCodes.isEmpty()) {
            return;
        }

        repository.deleteAllByCodeFiliere(codeFiliere);

        moduleClient.deleteModulesByCodesSemestre(semestresToDeleteCodes);
    }

    @Override
    @Transactional
    public void deleteAllByCodesFiliere(Set<String> codesFiliere) {

        Set<String> semestresToDeleteCodes = repository.findAllByCodeFiliereIn(codesFiliere)
                .stream()
                .map(Semestre::getCodeSemestre)
                .collect(Collectors.toSet());

        if (semestresToDeleteCodes.isEmpty()) {
            return;
        }

        repository.deleteAllByCodeFiliereIn(codesFiliere);

        moduleClient.deleteModulesByCodesSemestre(semestresToDeleteCodes);
    }
}
