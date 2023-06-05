package ma.enset.inscriptionpedagogique.service;

import ma.enset.inscriptionpedagogique.dto.*;
import ma.enset.inscriptionpedagogique.exception.DuplicateEntryException;
import ma.enset.inscriptionpedagogique.exception.ElementNotFoundException;
import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;

import java.util.List;
import java.util.Set;

public interface InscriptionPedagogiqueService {

    InscriptionResponse save(InscriptionCreationRequest request);

    List<InscriptionResponse> saveAll(List<InscriptionCreationRequest> request);

    boolean existAllByIds(Set<Long> ids) throws ElementNotFoundException;

    InscriptionResponse findById(Long id, boolean includeEtudiantInfo) throws ElementNotFoundException;

    List<InscriptionResponse> findAllByIds(Set<Long> ids, boolean includeEtudiantInfo) throws ElementNotFoundException;

    List<InscriptionResponse> findAllBySearchParams(RequiredSearchParams searchParams, boolean includeEtudiantInfo);

    InscriptionPagingResponse findAll(int page, int size, boolean includeEtudiantInfo);

    InscriptionResponse update(Long id, InscriptionUpdateRequest request) throws ElementNotFoundException;

    List<InscriptionResponse> updateAll(List<InscriptionUpdateRequest> request) throws ElementNotFoundException, DuplicateEntryException;

    void deleteById(Long id) throws ElementNotFoundException;

    void deleteAllByIds(Set<Long> ids) throws ElementNotFoundException;

    void deleteAllByCodeEtudiant(String codeEtudiant);

    void deleteAllByCodesEtudiant(Set<String> codesEtudiant);

    List<InscriptionPedagogique> findAll(RequiredSearchParams searchParams);

}
