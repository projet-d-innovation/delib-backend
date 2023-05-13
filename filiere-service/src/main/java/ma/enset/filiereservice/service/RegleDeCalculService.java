package ma.enset.filiereservice.service;

import ma.enset.filiereservice.dto.RegleDeCalculCreationRequest;
import ma.enset.filiereservice.dto.RegleDeCalculPagingResponse;
import ma.enset.filiereservice.dto.RegleDeCalculResponse;
import ma.enset.filiereservice.dto.RegleDeCalculUpdateRequest;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface RegleDeCalculService {
    RegleDeCalculResponse save(RegleDeCalculCreationRequest regleDeCalculCreationRequest) throws ElementAlreadyExistsException;

    List<RegleDeCalculResponse> saveAll(List<RegleDeCalculCreationRequest> regleDeCalculCreationRequests) throws ElementAlreadyExistsException;

    RegleDeCalculResponse findById(String id) throws ElementNotFoundException;

    List<RegleDeCalculResponse> findAllById(Set<String> ids) throws ElementNotFoundException;

    RegleDeCalculPagingResponse findAll(int page, int size);

    RegleDeCalculResponse update(String id, RegleDeCalculUpdateRequest regleDeCalculUpdateRequest) throws ElementNotFoundException;

    void deleteById(String codeRegleDeCalcul) throws ElementNotFoundException;

    void deleteById(Set<String> codesRegleDeCalculs) throws ElementNotFoundException;


}
