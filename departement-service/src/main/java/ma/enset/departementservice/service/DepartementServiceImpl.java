package ma.enset.departementservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.departementservice.constant.CoreConstants;
import ma.enset.departementservice.dto.DepartementCreationRequest;
import ma.enset.departementservice.dto.DepartementPagingResponse;
import ma.enset.departementservice.dto.DepartementResponse;
import ma.enset.departementservice.dto.DepartementUpdateRequest;
import ma.enset.departementservice.exception.ElementAlreadyExistsException;
import ma.enset.departementservice.exception.ElementNotFoundException;
import ma.enset.departementservice.exception.InternalErrorException;
import ma.enset.departementservice.mapper.DepartementMapper;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.repository.DepartementRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DepartementServiceImpl implements DepartementService {
    private final String ELEMENT_TYPE = "Departement";
    private final String ID_FIELD_NAME = "ID";

    private final DepartementMapper departementMapper;
    DepartementRepository departementRepository;

    @Override
    public DepartementResponse save(final DepartementCreationRequest departementCreationRequest) throws ElementAlreadyExistsException, InternalErrorException {
        if (departementRepository.existsById(departementCreationRequest.codeDepartement())) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, departementCreationRequest.codeDepartement()},
                    null
            );
        }

        final Departement departement = departementMapper.toDepartement(departementCreationRequest);

        if(departement.getCodeChefDepartement() != null) {
            // TODO (ahmed) : check if the chef departement exists else throw exception
        }

        Departement createdDepartement = null;

        try {
            createdDepartement = departementRepository.save(departement);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return departementMapper.toDepartementResponse(createdDepartement);
    }

    @Override
    @Transactional
    public List<DepartementResponse> saveAll(List<DepartementCreationRequest> departementCreationRequests) throws ElementAlreadyExistsException, InternalErrorException {


        final List<Departement> foundDepartements = departementRepository.findAllById(
                departementCreationRequests.stream()
                        .map(DepartementCreationRequest::codeDepartement)
                        .collect(Collectors.toSet())
        );

        if(!foundDepartements.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[] {ELEMENT_TYPE},
                    foundDepartements.stream()
                        .map(Departement::getCodeDepartement)
                        .collect(Collectors.toList())

            );
        }

        List<String> codeChefDepartements = departementCreationRequests.stream()
                .map(DepartementCreationRequest::codeChefDepartement)
                .toList();

        // TODO (ahmed) : check if the chef departement exists else throw exception

        List<Departement> departements = departementMapper.toDepartementList(departementCreationRequests);

        List<Departement> createdDepartements;

        try {
            createdDepartements = departementRepository.saveAll(departements);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return departementMapper.toDepartementResponseList(createdDepartements);
    }

    @Override
    public DepartementResponse findById(String id) throws ElementNotFoundException {
        return departementMapper.toDepartementResponse(
                departementRepository.findById(id)
                        .orElseThrow(() ->
                                new ElementNotFoundException(
                                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                    new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                                    null
                                ))
                );
    }

    @Override
    public List<DepartementResponse> findAllById(List<String> ids) throws ElementNotFoundException {

        final List<Departement> departements = departementRepository.findAllById(
                ids
        );

        List<String> notFoundIds = new ArrayList<>(ids);

        notFoundIds.removeAll(departements.stream()
                .map(Departement::getCodeDepartement)
                .toList());

        if(!notFoundIds.isEmpty()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[] {ELEMENT_TYPE},
                    notFoundIds
            );
        }

        return departementMapper.toDepartementResponseList(departements);
    }

    @Override
    public DepartementPagingResponse findAll(final int page, final int size,final String search) {
        return departementMapper.toPagingResponse(
                departementRepository.findAllByIntituleDepartementContainsIgnoreCase(search,PageRequest.of(page, size))
        );
    }

    @Override
    public DepartementResponse update(String id,DepartementUpdateRequest departementUpdateRequest) throws ElementNotFoundException, InternalErrorException {

        final Departement departement = departementRepository.findById(id)
                .orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                                null
                        ));

        departementMapper.updateDepartementFromDTO(departementUpdateRequest, departement);

        if(departement.getCodeChefDepartement() != null) {
            // TODO (ahmed) : check if the chef departement exists else throw exception
        }

        Departement updatedDepartement = null;

        try {
            updatedDepartement = departementRepository.save(departement);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return departementMapper.toDepartementResponse(updatedDepartement);
    }
    @Override
    public void deleteById(String id) throws ElementNotFoundException{
        if(!departementRepository.existsById(id)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
            );
        }

        departementRepository.deleteById(id);
    }

    @Override
    public void deleteById(List<String> ids) throws ElementNotFoundException{

        this.findAllById(ids);
        departementRepository.deleteAllById(ids);

    }

}
