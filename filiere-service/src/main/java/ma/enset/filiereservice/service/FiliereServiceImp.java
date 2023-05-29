package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.client.DepartementClient;
import ma.enset.filiereservice.client.SemestreClient;
import ma.enset.filiereservice.client.UtilisateurClient;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.dto.*;
import ma.enset.filiereservice.exception.DuplicateEntryException;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
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
    private final DepartementClient departementClient;
    private final SemestreClient semestreClient;
    private final UtilisateurClient utilisateurClient;

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

        departementClient.exists(
                Set.of(filiereCreationRequest.codeDepartement())
        );

        if (filiereCreationRequest.codeRegleDeCalcul() != null) {
            regleDeCalculService.findById(filiereCreationRequest.codeRegleDeCalcul());
        }

        if (filiere.getCodeChefFiliere() != null) {
            utilisateurClient.existsById(
                    Set.of(filiere.getCodeChefFiliere())
            );
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

        departementClient.exists(
                filiereResponseList.stream()
                        .map(FiliereCreationRequest::codeDepartement)
                        .collect(Collectors.toSet())
        );

        Set<String> codeRegleDeCalculs = filiereResponseList.stream()
                .map(FiliereCreationRequest::codeRegleDeCalcul)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        if (!codeRegleDeCalculs.isEmpty()) {
            regleDeCalculService.findAllById(codeRegleDeCalculs);
        }


        Set<String> codeChefFilieres = filiereResponseList.stream()
                .map(FiliereCreationRequest::codeChefFiliere)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        if (!codeChefFilieres.isEmpty()) {
            utilisateurClient.existsById(codeChefFilieres);
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
            List<SemestreResponse> semestreResponses = semestreClient.getAllByCodeFiliere(
                    filiereResponse.getCodeFiliere()
            ).getBody();
            filiereResponse.setSemestres(semestreResponses);
        }
        if (includeRegleDeCalcule && filiereResponse.getCodeRegleDeCalcul() != null) {
            RegleDeCalculResponse regleDeCalculResponse = regleDeCalculService.findById(filiereResponse.getCodeRegleDeCalcul());
            filiereResponse.setRegleDeCalcul(regleDeCalculResponse);
        }

        if (includeChefFiliere) {
            filiereResponse.setChefFiliere(
                    utilisateurClient.findById(filiereResponse.getCodeChefFiliere()).getBody()
            );
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
            Set<String> codeFilieres = filiereResponses.stream()
                    .map(FiliereResponse::getCodeFiliere)
                    .collect(Collectors.toSet());

            if (!codeFilieres.isEmpty()) {
                List<GroupedSemestresResponse> semestreResponses = semestreClient.getAllByCodeFilieres(codeFilieres).getBody();

                if (semestreResponses != null && !semestreResponses.isEmpty()) {
                    filiereResponses.forEach(filiereResponse -> filiereResponse.setSemestres(
                            Objects.requireNonNull(semestreResponses.stream()
                                            .filter(groupedSemestresResponse -> groupedSemestresResponse.codeFiliere()
                                                    .equals(
                                                            filiereResponse.getCodeFiliere()
                                                    )
                                            )
                                            .findFirst()
                                            .orElse(null))
                                    .semestres()
                    ));
                }
            }
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
            Set<String> chefCodes = filiereResponses.stream()
                    .map(FiliereResponse::getCodeChefFiliere)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (!chefCodes.isEmpty()) {
                List<UtilisateurResponse> utilisateurResponses = utilisateurClient.findAllById(chefCodes).getBody();
                if (utilisateurResponses != null && !utilisateurResponses.isEmpty()) {
                    filiereResponses.forEach(filiereResponse -> filiereResponse.setChefFiliere(
                            utilisateurResponses.stream()
                                    .filter(utilisateurResponse -> utilisateurResponse.getCode()
                                            .equals(
                                                    filiereResponse.getCodeChefFiliere()
                                            )
                                    )
                                    .findFirst()
                                    .orElse(null)
                    ));
                }
            }
        }
        return filiereResponses;
    }

    @Override
    public FilierePagingResponse findAll(int page, int size, String search,
                                         boolean includeSemestre,
                                         boolean includeRegleDeCalcule,
                                         boolean includeChefFiliere) {

        Filiere filiereExample = new Filiere();
        filiereExample.setIntituleFiliere(search);

        FilierePagingResponse filierePagingResponse = filiereMapper.toPagingResponse(
                filiereRepository.findAll(
                        Example.of(filiereExample, FILIERE_EXAMPLE_MATCHER),
                        PageRequest.of(page, size)
                )
        );
        if (includeSemestre) {
            Set<String> codeFilieres = filierePagingResponse.records().stream()
                    .map(FiliereResponse::getCodeFiliere)
                    .collect(Collectors.toSet());

            if (!codeFilieres.isEmpty()) {
                List<GroupedSemestresResponse> semestreResponses = semestreClient.getAllByCodeFilieres(codeFilieres).getBody();
                if (semestreResponses != null && !semestreResponses.isEmpty()) {
                    filierePagingResponse.records().forEach(filiereResponse -> filiereResponse.setSemestres(
                            Objects.requireNonNull(semestreResponses.stream()
                                            .filter(groupedSemestresResponse -> groupedSemestresResponse.codeFiliere()
                                                    .equals(
                                                            filiereResponse.getCodeFiliere()
                                                    )
                                            )
                                            .findFirst()
                                            .orElse(null))
                                    .semestres()
                    ));
                }
            }
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
                if (!regleDeCalculResponses.isEmpty()) {
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
        }

        if (includeChefFiliere) {
            Set<String> chefCodes = filierePagingResponse.records().stream()
                    .map(FiliereResponse::getCodeChefFiliere)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!chefCodes.isEmpty()) {
                List<UtilisateurResponse> utilisateurResponses = utilisateurClient.findAllById(chefCodes).getBody();
                if (utilisateurResponses != null) {
                    filierePagingResponse.records().forEach(filiereResponse -> filiereResponse.setChefFiliere(
                            utilisateurResponses.stream()
                                    .filter(utilisateurResponse -> utilisateurResponse.getCode()
                                            .equals(
                                                    filiereResponse.getCodeChefFiliere()
                                            )
                                    )
                                    .findFirst()
                                    .orElse(null)
                    ));
                }
            }
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
            utilisateurClient.existsById(Set.of(filiereUpdateRequest.codeChefFiliere()));
        }

        return filiereMapper.toFiliereResponse(filiereRepository.save(filiere));

    }

    @Override
    @Transactional
    public void deleteById(String id) throws ElementNotFoundException {
        if (!filiereRepository.existsById(id)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
            );
        }

        semestreClient.deleteAllByCodeFiliere(id);

        filiereRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void deleteById(Set<String> ids) throws ElementNotFoundException {
        this.findAllById(ids, false, false, false);

        semestreClient.deleteAllByCodesFiliere(ids);

        filiereRepository.deleteAllById(ids);
    }

    @Override
    @Transactional
    public void deleteByCodeDepartement(String codeDepartement) throws ElementNotFoundException {

        List<Filiere> foundFilieres = filiereRepository.findAllByCodeDepartement(codeDepartement);

        semestreClient.deleteAllByCodesFiliere(
                foundFilieres.stream()
                        .map(Filiere::getCodeFiliere)
                        .collect(Collectors.toSet())
        );

        filiereRepository.deleteAll(foundFilieres);
    }

    @Override
    @Transactional
    public void deleteByCodeDepartement(Set<String> codes) throws ElementNotFoundException {
        List<Filiere> foundFilieres = filiereRepository.findAllByCodeDepartementIn(codes);

        Set<String> ids = foundFilieres.stream()
                .map(Filiere::getCodeFiliere)
                .collect(Collectors.toSet());


        semestreClient.deleteAllByCodesFiliere(ids);

        filiereRepository.deleteAllById(ids);
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


    @Override
    public boolean existsAllId(Set<String> codesFiliere) throws ElementNotFoundException {

        List<String> foundFiliereCodes = filiereRepository.findAllById(codesFiliere)
                .stream().map(Filiere::getCodeFiliere).toList();

        if (codesFiliere.size() != foundFiliereCodes.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codesFiliere.stream()
                            .filter(code -> !foundFiliereCodes.contains(code))
                            .toList()
            );
        }

        return true;
    }

    @Override
    public void handleUtilisateurDeletion(Set<String> codeUtilisateurs) {
        List<Filiere> filieres = filiereRepository.findAllByCodeChefFiliereIn(codeUtilisateurs);

        filieres.forEach(filiere -> {
            filiere.setCodeChefFiliere(null);
        });

        filiereRepository.saveAll(filieres);
    }
}
