package ma.enset.inscriptionpedagogique.service;

import lombok.RequiredArgsConstructor;
import ma.enset.inscriptionpedagogique.client.FiliereClient;
import ma.enset.inscriptionpedagogique.client.SessionUniversitaireClient;
import ma.enset.inscriptionpedagogique.client.UtilisateurClient;
import ma.enset.inscriptionpedagogique.constant.CoreConstants;
import ma.enset.inscriptionpedagogique.dto.*;
import ma.enset.inscriptionpedagogique.exception.ApiClientException;
import ma.enset.inscriptionpedagogique.exception.DuplicateEntryException;
import ma.enset.inscriptionpedagogique.exception.ElementNotFoundException;
import ma.enset.inscriptionpedagogique.exception.handler.dto.ExceptionResponse;
import ma.enset.inscriptionpedagogique.mapper.InscriptionPedagogiqueMapper;
import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;
import ma.enset.inscriptionpedagogique.repository.InscriptionPedagogiqueRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscriptionPedagogiqueServiceImpl implements InscriptionPedagogiqueService {

    private final String ELEMENT_TYPE = "InscriptionPedagogique";
    private final String ID_FIELD_NAME = "id";
    private final UtilisateurClient utilisateurClient;
    private final FiliereClient filiereClient;
    private final SessionUniversitaireClient sessionUniversitaireClient;
    private final InscriptionPedagogiqueMapper mapper;
    private final InscriptionPedagogiqueRepository repository;

    @Override
    public InscriptionResponse save(InscriptionCreationRequest request) {

        allEtudiantsExist(Set.of(request.codeEtudiant()));

        filiereClient.allFilieresExist(Set.of(request.codeFiliere()));

        sessionUniversitaireClient.allSessionsUniversitaireExist(Set.of(request.codeSessionUniversitaire()));

        InscriptionPedagogique inscription = mapper.toInscriptionEntity(request);

        return mapper.toInscriptionResponse(repository.save(inscription));
    }

    @Override
    public List<InscriptionResponse> saveAll(List<InscriptionCreationRequest> request) {

        allEtudiantsExist(
            request.stream()
                .map(InscriptionCreationRequest::codeEtudiant)
                .collect(Collectors.toSet())
        );

        filiereClient.allFilieresExist(
            request.stream()
                .map(InscriptionCreationRequest::codeFiliere)
                .collect(Collectors.toSet())
        );

        sessionUniversitaireClient.allSessionsUniversitaireExist(
            request.stream()
                .map(InscriptionCreationRequest::codeSessionUniversitaire)
                .collect(Collectors.toSet())
        );

        List<InscriptionPedagogique> inscriptions = mapper.toInscriptionEntityList(request);

        return mapper.toInscriptionResponseList(repository.saveAll(inscriptions));
    }

    @Override
    public boolean existAllByIds(Set<Long> ids) throws ElementNotFoundException {

        List<Long> foundInscriptions = repository.findAllById(ids)
            .stream().map(InscriptionPedagogique::getId).toList();

        if (ids.size() != foundInscriptions.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                ids.stream()
                    .filter(id -> !foundInscriptions.contains(id))
                    .map(String::valueOf)
                    .toList()
            );
        }

        return true;
    }

    @Override
    public InscriptionResponse findById(Long id, boolean includeEtudiantInfo) throws ElementNotFoundException {

        InscriptionResponse response = mapper.toInscriptionResponse(
            repository.findById(id).orElseThrow(() ->
                new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
                )
            )
        );

        if (includeEtudiantInfo) {
            mapper.fillStudentInfo(
                utilisateurClient.getEtudiantById(response.etudiant().getCode()).getBody(),
                response.etudiant()
            );
        }

        return response;
    }

    @Override
    public List<InscriptionResponse> findAllByIds(Set<Long> ids, boolean includeEtudiantInfo) throws ElementNotFoundException {

        List<InscriptionResponse> response = mapper.toInscriptionResponseList(repository.findAllById(ids));

        if (ids.size() != response.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                ids.stream()
                    .filter(
                        id -> !response.stream()
                                        .map(InscriptionResponse::id)
                                        .toList()
                                        .contains(id)
                    )
                    .map(String::valueOf)
                    .toList()
            );
        }

        handleEtudiantInfoInclusion(includeEtudiantInfo, response);

        return response;
    }

    @Override
    public List<InscriptionResponse> findAllBySearchParams(RequiredSearchParams searchParams, boolean includeEtudiantInfo) {

        List<InscriptionResponse> response = mapper.toInscriptionResponseList(findAll(searchParams));

        if (!response.isEmpty()) {
            handleEtudiantInfoInclusion(includeEtudiantInfo, response);
        }
        
        return response;
    }

    @Override
    public InscriptionPagingResponse findAll(int page, int size, boolean includeEtudiantInfo) {

        InscriptionPagingResponse response = mapper.toPagingResponse(repository.findAll(PageRequest.of(page, size)));

        if (includeEtudiantInfo && !response.records().isEmpty()) {
            Set<String> codesEtudiant = response.records().stream()
                .map(inscriptionResponse -> inscriptionResponse.etudiant().getCode())
                .collect(Collectors.toSet());

            mapper.fillInscriptionResponseListStudentInfo(
                Objects.requireNonNull(utilisateurClient.getAllEtudiantsByIds(codesEtudiant).getBody()),
                response.records()
            );
        }

        return response;
    }

    @Override
    public InscriptionResponse update(Long id, InscriptionUpdateRequest request) throws ElementNotFoundException {

        InscriptionPedagogique inscription = repository.findById(id).orElseThrow(() ->
            new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                null
            )
        );

        mapper.updateInscriptionEntityFromDTO(request, inscription);

        return mapper.toInscriptionResponse(repository.save(inscription));
    }

    @Override
    public List<InscriptionResponse> updateAll(List<InscriptionUpdateRequest> request) throws ElementNotFoundException, DuplicateEntryException {

        Set<Long> inscriptionIds = request.stream()
            .map(InscriptionUpdateRequest::id).collect(Collectors.toSet());

        if (inscriptionIds.size() != request.size()) {
            throw new DuplicateEntryException(
                CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                new Object[]{ELEMENT_TYPE}
            );
        }

        List<InscriptionPedagogique> inscriptions = repository.findAllById(inscriptionIds);

        if (inscriptions.size() != inscriptionIds.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                inscriptionIds.stream()
                    .filter(
                        id -> !inscriptions.stream()
                                            .map(InscriptionPedagogique::getId)
                                            .toList()
                                            .contains(id)
                    )
                    .map(String::valueOf)
                    .toList()
            );
        }

        mapper.updateInscriptionEntityListFromDTOList(request, inscriptions);

        return mapper.toInscriptionResponseList(inscriptions);
    }

    @Override
    public void deleteById(Long id) throws ElementNotFoundException {

        if (!repository.existsById(id)) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                null
            );
        }

        repository.deleteById(id);

        // TODO: delete related data
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) throws ElementNotFoundException {
        existAllByIds(ids);

        repository.deleteAllById(ids);

        // TODO: delete related data
    }

    @Override
    public void deleteAllByCodeEtudiant(String codeEtudiant) {
        repository.deleteAllByCodeEtudiant(codeEtudiant);

        // TODO: delete related data
    }

    @Override
    public void deleteAllByCodesEtudiant(Set<String> codesEtudiant) {
        repository.deleteAllByCodeEtudiantIn(codesEtudiant);

        // TODO: delete related data
    }

    private void handleEtudiantInfoInclusion(boolean includeEtudiantInfo, List<InscriptionResponse> response) {

        if (includeEtudiantInfo) {
            Set<String> codesEtudiant = response.stream()
                .map(inscriptionResponse -> inscriptionResponse.etudiant().getCode())
                .collect(Collectors.toSet());

            mapper.fillInscriptionResponseListStudentInfo(
                Objects.requireNonNull(utilisateurClient.getAllEtudiantsByIds(codesEtudiant).getBody()),
                response
            );
        }
    }

    @Override
    public List<InscriptionPedagogique> findAll(RequiredSearchParams searchParams) {

        InscriptionPedagogique inscription = mapper.toInscriptionEntity(searchParams);

        return repository.findAll(buildQueryExample(inscription));
    }

    private Example<InscriptionPedagogique> buildQueryExample(InscriptionPedagogique inscription) {

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnorePaths("note");

        return Example.of(inscription, matcher);
    }

    private void allEtudiantsExist(Set<String> codesEtudiant) throws ApiClientException {
        try {
            utilisateurClient.allEtudiantsExist(codesEtudiant);
        } catch (ApiClientException e) {
            injectCustomEtudiantMessage(e.getException());
            throw new ApiClientException(e.getException());
        }
    }

    private void injectCustomEtudiantMessage(ExceptionResponse response) {
        String customMessage = response.getMessage()
            .replaceAll("(Utilisateur|User)", "Etudiant");

        response.setMessage(customMessage);
    }
}
