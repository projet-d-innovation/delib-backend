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

    boolean existAllByIds(Set<String> ids) throws ElementNotFoundException;

    InscriptionResponse findById(String id, boolean includeEtudiantInfo) throws ElementNotFoundException;

    List<InscriptionResponse> findAllByIds(Set<String> ids, boolean includeEtudiantInfo) throws ElementNotFoundException;

    List<InscriptionResponse> findAllBySearchParams(RequiredSearchParams searchParams, boolean includeEtudiantInfo);

    InscriptionPagingResponse findAll(int page, int size, boolean includeEtudiantInfo);

    InscriptionResponse update(String id, InscriptionUpdateRequest request) throws ElementNotFoundException;

    List<InscriptionResponse> updateAll(List<InscriptionUpdateRequest> request) throws ElementNotFoundException, DuplicateEntryException;

    void deleteById(String id) throws ElementNotFoundException;

    void deleteAllByIds(Set<String> ids) throws ElementNotFoundException;

    void deleteAllByCodeEtudiant(String codeEtudiant);

    void deleteAllByCodesEtudiant(Set<String> codesEtudiant);

    List<InscriptionPedagogique> findAll(RequiredSearchParams searchParams);

}
