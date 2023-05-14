package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.dto.RegleDeCalculCreationRequest;
import ma.enset.filiereservice.dto.RegleDeCalculPagingResponse;
import ma.enset.filiereservice.dto.RegleDeCalculResponse;
import ma.enset.filiereservice.dto.RegleDeCalculUpdateRequest;
import ma.enset.filiereservice.exception.DuplicateEntryException;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.mapper.RegleDeCalculMapper;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.model.RegleDeCalcul;
import ma.enset.filiereservice.repository.FiliereRepository;
import ma.enset.filiereservice.repository.RegleDeCalculRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RegleDeCalculServiceImpl implements RegleDeCalculService {

    private final String ELEMENT_TYPE = "RegleDeCalcul";
    private final String ID_FIELD_NAME = "codeRegleDeCalcul";
    private final RegleDeCalculRepository regleDeCalculRepository;
    private final RegleDeCalculMapper regleDeCalculMapper;

    private final FiliereRepository filiereRepository;

    @Override
    public RegleDeCalculResponse save(RegleDeCalculCreationRequest regleDeCalculCreationRequest) throws ElementAlreadyExistsException {

        if (regleDeCalculRepository.existsById(regleDeCalculCreationRequest.codeRegleDeCalcul())) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, regleDeCalculCreationRequest.codeRegleDeCalcul()},
                    null
            );
        }

        RegleDeCalcul regleDeCalcul = regleDeCalculMapper.toRegleDeCalcul(regleDeCalculCreationRequest);

        RegleDeCalcul createdRegleDeCalcul = null;

        try {
            createdRegleDeCalcul = regleDeCalculRepository.save(regleDeCalcul);
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, regleDeCalculCreationRequest.codeRegleDeCalcul()},
                    null
            );
        }
        return regleDeCalculMapper.toRegleDeCalculResponse(createdRegleDeCalcul);
    }

    @Override
    @Transactional
    public List<RegleDeCalculResponse> saveAll(List<RegleDeCalculCreationRequest> regleDeCalculCreationRequests) throws ElementAlreadyExistsException {
        final List<RegleDeCalcul> foundRegleDeCalculs = regleDeCalculRepository.findAllById(
                regleDeCalculCreationRequests.stream()
                        .map(RegleDeCalculCreationRequest::codeRegleDeCalcul)
                        .collect(Collectors.toSet())
        );

        if (!foundRegleDeCalculs.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE},
                    foundRegleDeCalculs.stream()
                            .map(RegleDeCalcul::getCodeRegleDeCalcul)
                            .toList()
            );
        }

        List<RegleDeCalcul> regleDeCalculs = regleDeCalculMapper.toRegleDeCalculList(regleDeCalculCreationRequests);

        List<RegleDeCalcul> createdRegleDeCalculs = null;

        try {
            createdRegleDeCalculs = regleDeCalculRepository.saveAll(regleDeCalculs);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    null
            );
        }

        return regleDeCalculMapper.toRegleDeCalculResponseList(createdRegleDeCalculs);
    }

    @Override
    public RegleDeCalculResponse findById(String id) throws ElementNotFoundException {
        return regleDeCalculRepository.findById(id)
                .map(regleDeCalculMapper::toRegleDeCalculResponse)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));
    }

    @Override
    public List<RegleDeCalculResponse> findAllById(Set<String> ids) throws ElementNotFoundException {
        final List<RegleDeCalcul> foundRegleDeCalculs = regleDeCalculRepository.findAllById(ids);
        final List<String> foundIds = foundRegleDeCalculs.stream()
                .map(RegleDeCalcul::getCodeRegleDeCalcul)
                .toList();
        if (foundRegleDeCalculs.size() != ids.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    ids.stream()
                            .filter(it -> !foundIds.contains(it))
                            .toList()
            );
        }

        return regleDeCalculMapper.toRegleDeCalculResponseList(foundRegleDeCalculs);
    }

    @Override
    public RegleDeCalculPagingResponse findAll(int page, int size) {
        return regleDeCalculMapper.toPagingResponse(
                regleDeCalculRepository.findAll(PageRequest.of(page, size))
        );
    }

    @Override
    public RegleDeCalculResponse update(String id, RegleDeCalculUpdateRequest regleDeCalculUpdateRequest) throws ElementNotFoundException {
        RegleDeCalcul regleDeCalcul = regleDeCalculRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));

        regleDeCalculMapper.updateRegleDeCalculFromDto(regleDeCalculUpdateRequest, regleDeCalcul);

        return regleDeCalculMapper.toRegleDeCalculResponse(regleDeCalculRepository.save(regleDeCalcul));
    }

    @Override
    public void deleteById(String codeRegleDeCalcul) throws ElementNotFoundException {

        if (!regleDeCalculRepository.existsById(codeRegleDeCalcul)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeRegleDeCalcul},
                    null
            );
        }

        this.handleRegleCodeChangeDelete(codeRegleDeCalcul);
        regleDeCalculRepository.deleteById(codeRegleDeCalcul);
    }

    @Override
    @Transactional
    public void deleteById(Set<String> codesRegleDeCalculs) throws ElementNotFoundException {
        this.findAllById(codesRegleDeCalculs);
        this.handleRegleCodeChangeDelete(codesRegleDeCalculs);
        regleDeCalculRepository.deleteAllById(codesRegleDeCalculs);
    }


    private void handleRegleCodeChangeDelete(String codeRegleDeCalcul) {
        List<Filiere> filieres = filiereRepository.findAllByCodeRegleDeCalcul(codeRegleDeCalcul);
        filieres.forEach(filiere -> {
            filiere.setCodeRegleDeCalcul(null);
        });
        filiereRepository.saveAll(filieres);
    }

    private void handleRegleCodeChangeDelete(Set<String> codeRegleDeCalcul) {
        List<Filiere> filieres = filiereRepository.findAllByCodeRegleDeCalculIn(codeRegleDeCalcul);
        filieres.forEach(filiere -> {
            filiere.setCodeRegleDeCalcul(null);
        });
        filiereRepository.saveAll(filieres);
    }
}
