package ma.enset.departementservice.service;

import ma.enset.departementservice.dto.DepartementCreationRequest;
import ma.enset.departementservice.dto.DepartementPagingResponse;
import ma.enset.departementservice.dto.DepartementResponse;
import ma.enset.departementservice.dto.DepartementUpdateRequest;
import ma.enset.departementservice.exception.ElementAlreadyExistsException;
import ma.enset.departementservice.exception.ElementNotFoundException;
import ma.enset.departementservice.exception.InternalErrorException;
import ma.enset.departementservice.model.Departement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DepartementService {
    DepartementResponse save(DepartementCreationRequest departement) throws ElementAlreadyExistsException;

    List<DepartementResponse> saveAll(List<DepartementCreationRequest> departements) throws ElementAlreadyExistsException;

    DepartementResponse findById(String id, boolean includeFilieres) throws ElementNotFoundException;

    List<DepartementResponse> findAllById(Set<String> ids, boolean includeFilieres) throws ElementNotFoundException;

    DepartementPagingResponse findAll(int page, int size, String search, boolean includeFilieres);

    DepartementResponse update(String id, DepartementUpdateRequest departement) throws ElementNotFoundException;

    void deleteById(String id) throws ElementNotFoundException;

    void deleteById(Set<String> ids) throws ElementNotFoundException;


}
