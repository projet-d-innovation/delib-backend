package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.dto.*;
import ma.enset.filiereservice.exception.DuplicateEntryException;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.mapper.FiliereMapper;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.repository.FiliereRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j

public class FiliereServiceImp implements FiliereService {
    private final String ELEMENT_TYPE = "Filiere";
    private final String ID_FIELD_NAME = "codeFiliere";
    private final FiliereMapper filiereMapper;
    private final FiliereRepository filiereRepository;
    private final RegleDeCalculService regleDeCalculService;

    private final static ExampleMatcher FILIERE_EXAMPLE_MATCHER = ExampleMatcher.matching()
            .withIgnorePaths("codeChefFiliere", "codeRegleDeCalcul", "codeDepartement")
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

    @Override
    public FiliereResponse save(final FiliereCreationRequest filiereCreationRequest) throws ElementAlreadyExistsException {
        if (filiereRepository.existsById(filiereCreationRequest.codeFiliere())) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, filiereCreationRequest.codeDepartement()},
                    null
            );
        }
        Filiere filiere = filiereMapper.toFiliere(filiereCreationRequest);

        // TODO (ahmed) check if departement exists

        if (filiereCreationRequest.codeRegleDeCalcul() != null) {
            regleDeCalculService.findById(filiereCreationRequest.codeRegleDeCalcul());
        }

        if (filiere.getCodeChefFiliere() != null) {
            // TODO (ahmed) check if chef filiere exists
        }

        Filiere createdFiliere = null;

        try {
            createdFiliere = filiereRepository.save(filiere);
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, filiereCreationRequest.codeFiliere()},
                    null
            );
        }
        return filiereMapper.toFiliereResponse(createdFiliere);
    }

    @Override
    @Transactional
    public List<FiliereResponse> saveAll(List<FiliereCreationRequest> filiereResponseList) throws ElementAlreadyExistsException {
        final List<Filiere> foundFilieres = filiereRepository.findAllById(
                filiereResponseList.stream()
                        .map(FiliereCreationRequest::codeFiliere)
                        .collect(Collectors.toList())
        );


        if (!foundFilieres.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE},
                    foundFilieres.stream()
                            .map(Filiere::getCodeFiliere)
                            .collect(Collectors.toList())
            );
        }

        Set<String> codeRegleDeCalculs = filiereResponseList.stream()
                .map(FiliereCreationRequest::codeRegleDeCalcul)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!codeRegleDeCalculs.isEmpty()) {
            regleDeCalculService.findAllById(codeRegleDeCalculs);
        }

        Set<String> codeDepartements = filiereResponseList.stream()
                .map(FiliereCreationRequest::codeDepartement)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!codeDepartements.isEmpty()) {
            // TODO (ahmed) check if departement exists
        }

        Set<String> codeChefFilieres = filiereResponseList.stream()
                .map(FiliereCreationRequest::codeChefFiliere)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!codeChefFilieres.isEmpty()) {
            // TODO (ahmed) check if chef filiere exists
        }


        List<Filiere> filieres = filiereMapper.toFiliereList(filiereResponseList);

        List<Filiere> createdFilieres;
        try {
            createdFilieres = filiereRepository.saveAll(filieres);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    null
            );
        }

        return filiereMapper.toFiliereResponseList(createdFilieres);

    }

    @Override
    public FiliereResponse findById(String id,
                                    boolean includeSemestre,
                                    boolean includeRegleDeCalcule,
                                    boolean includeChefFiliere) throws ElementNotFoundException {
        FiliereResponse filiereResponse = filiereRepository.findById(id)
                .map(filiereMapper::toFiliereResponse)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));

        if (includeSemestre) {
            // TODO (ahmed) : get semestres
        }
        if (includeRegleDeCalcule && filiereResponse.getCodeRegleDeCalcul() != null) {
            RegleDeCalculResponse regleDeCalculResponse = regleDeCalculService.findById(filiereResponse.getCodeRegleDeCalcul());
            filiereResponse.setRegleDeCalcul(regleDeCalculResponse);
        }

        if (includeChefFiliere) {
            // TODO (ahmed) : get chef filiere
        }

        return filiereResponse;
    }

    @Override
    public List<FiliereResponse> findAllById(Set<String> ids,
                                             boolean includeSemestre,
                                             boolean includeRegleDeCalcule,
                                             boolean includeChefFiliere) throws ElementNotFoundException {

        final List<Filiere> filieres = filiereRepository.findAllById(ids);

        List<String> notFoundIds = new ArrayList<>(ids);

        notFoundIds.removeAll(filieres.stream()
                .map(Filiere::getCodeFiliere)
                .toList());

        if (!notFoundIds.isEmpty()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    notFoundIds
            );
        }

        List<FiliereResponse> filiereResponses = filiereMapper.toFiliereResponseList(filieres);

        if (includeSemestre) {
            // TODO (ahmed) : get semestres
        }
        if (includeRegleDeCalcule) {
            Set<String> codeRegleDeCalculs = new HashSet<>();

            filiereResponses.stream()
                    .map(FiliereResponse::getCodeRegleDeCalcul)
                    .forEach(code -> {
                        if (code != null) {
                            codeRegleDeCalculs.add(code);
                        }
                    });
            if (!codeRegleDeCalculs.isEmpty()) {
                List<RegleDeCalculResponse> regleDeCalculResponses = regleDeCalculService.findAllById(codeRegleDeCalculs);
                filiereResponses.forEach(filiereResponse -> filiereResponse.setRegleDeCalcul(
                        regleDeCalculResponses.stream()
                                .filter(regleDeCalculResponse -> regleDeCalculResponse.codeRegleDeCalcul().equals(filiereResponse.getCodeRegleDeCalcul()))
                                .findFirst()
                                .orElse(null)
                ));
            }
        }
        if (includeChefFiliere) {
            // TODO (ahmed) : get chef filiere
        }

        return filiereResponses;
    }

    @Override
    public FilierePagingResponse findAll(int page, int size, String search,
                                         boolean includeSemestre,
                                         boolean includeRegleDeCalcule,
                                         boolean includeChefFiliere) {

        Filiere filiereExample = Filiere.builder()
                .intituleFiliere(search)
                .build();

        FilierePagingResponse filierePagingResponse = filiereMapper.toPagingResponse(
                filiereRepository.findAll(
                        Example.of(filiereExample, FILIERE_EXAMPLE_MATCHER),
                        PageRequest.of(page, size)
                )
        );

        if (includeSemestre) {
            // TODO (ahmed) : get semestres
        }
        if (includeRegleDeCalcule) {
            Set<String> codeRegleDeCalculs = new HashSet<>();

            filierePagingResponse.records()
                    .stream()
                    .map(FiliereResponse::getCodeRegleDeCalcul)
                    .forEach(code -> {
                        if (code != null) {
                            codeRegleDeCalculs.add(code);
                        }
                    });

            if (!codeRegleDeCalculs.isEmpty()) {
                List<RegleDeCalculResponse> regleDeCalculResponses = regleDeCalculService.findAllById(codeRegleDeCalculs);
                filierePagingResponse.records().forEach(
                        filiereResponse -> filiereResponse.setRegleDeCalcul(
                                regleDeCalculResponses.stream()
                                        .filter(regleDeCalculResponse -> regleDeCalculResponse.codeRegleDeCalcul().equals(filiereResponse.getCodeRegleDeCalcul()))
                                        .findFirst()
                                        .orElse(null)
                        )
                );
            }
        }

        if (includeChefFiliere) {
            // TODO (ahmed) : get chef filiere
        }

        return filierePagingResponse;
    }

    @Override
    public FiliereResponse update(String id, FiliereUpdateRequest filiereUpdateRequest) throws ElementNotFoundException {
        final Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));

        filiereMapper.updateFiliereFromDto(filiereUpdateRequest, filiere);

        if (filiereUpdateRequest.codeRegleDeCalcul() != null) {
            regleDeCalculService.findById(filiereUpdateRequest.codeRegleDeCalcul());
        }

        if (filiereUpdateRequest.codeChefFiliere() != null) {
            // TODO (ahmed) : check if chef filiere exists
        }

        Filiere updatedFiliere = null;

        try {
            updatedFiliere = filiereRepository.save(filiere);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return filiereMapper.toFiliereResponse(updatedFiliere);

    }

    @Override
    public void deleteById(String id) throws ElementNotFoundException {
        if (!filiereRepository.existsById(id)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
            );
        }
        filiereRepository.deleteById(id);

        // TODO (ahmed) : delete semestres
    }


    @Override
    @Transactional
    public void deleteById(Set<String> ids) throws ElementNotFoundException {
        this.findAllById(ids, false, false, false);
        filiereRepository.deleteAllById(ids);

        // TODO (ahmed) : delete semestres
    }

    @Override
    public void deleteByCodeDepartement(String codeDepartement) throws ElementNotFoundException {
        List<Filiere> foundFilieres = filiereRepository.findAllByCodeDepartement(codeDepartement);
        if (foundFilieres.isEmpty()) return;
        filiereRepository.deleteAll(foundFilieres);
    }

    @Override
    public void deleteByCodeDepartement(Set<String> codes) throws ElementNotFoundException {
        List<Filiere> foundFilieres = filiereRepository.findAllByCodeDepartementIn(codes);
        if (foundFilieres.isEmpty()) return;
        filiereRepository.deleteAll(foundFilieres);
    }


    @Override
    public FiliereByDepartementResponse findByCodeDepartement(String codeDepartement, boolean includeSemestre, boolean includeRegleDeCalcule, boolean includeChefFiliere) throws ElementNotFoundException {

        Set<String> codeFilieres = filiereRepository.findAllByCodeDepartement(codeDepartement)
                .stream()
                .map(Filiere::getCodeFiliere)
                .collect(Collectors.toSet());

        return FiliereByDepartementResponse.builder()
                .codeDepartement(codeDepartement)
                .filieres(this.findAllById(codeFilieres, includeSemestre, includeRegleDeCalcule, includeChefFiliere))
                .build();
    }

    @Override
    public List<FiliereByDepartementResponse> findAllByCodeDepartement(Set<String> codeDepartement, boolean includeSemestre, boolean includeRegleDeCalcule, boolean includeChefFiliere) throws ElementNotFoundException {
        Set<String> codeFilieres = filiereRepository.findAllByCodeDepartementIn(codeDepartement)
                .stream()
                .map(Filiere::getCodeFiliere)
                .collect(Collectors.toSet());

        List<FiliereResponse> filiereResponses = this.findAllById(codeFilieres, includeSemestre, includeRegleDeCalcule, includeChefFiliere);

        List<FiliereByDepartementResponse> filiereByDepartementResponses = new ArrayList<>();

        for (String code : codeDepartement) {
            filiereByDepartementResponses.add(
                    FiliereByDepartementResponse.builder()
                            .codeDepartement(code)
                            .filieres(
                                    filiereResponses.stream()
                                            .filter(filiereResponse -> filiereResponse.getCodeDepartement().equals(code))
                                            .collect(Collectors.toList())
                            )
                            .build()
            );
        }
        return filiereByDepartementResponses;
    }
}
