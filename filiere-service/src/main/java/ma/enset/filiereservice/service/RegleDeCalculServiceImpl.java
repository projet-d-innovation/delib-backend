package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.model.RegleDeCalcul;
import ma.enset.filiereservice.repository.RegleDeCalculRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RegleDeCalculServiceImpl implements RegleDeCalculService {
    RegleDeCalculRepository regleDeCalculRepository;

    @Override
    public RegleDeCalcul save(RegleDeCalcul regleDeCalcul) throws ElementAlreadyExistsException, InternalErrorException {
        if (regleDeCalculRepository.existsByCodeRegle(regleDeCalcul.getCodeRegle())) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.REGLE_DE_CALCUL_ALREADY_EXISTS)
                    .args(new Object[]{regleDeCalcul.getCodeRegle()})
                    .build();
        }

        RegleDeCalcul createdRegleDeCalcul = null;

        try {
            createdRegleDeCalcul = regleDeCalculRepository.save(regleDeCalcul);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return createdRegleDeCalcul;
    }

    @Override
    @Transactional
    public List<RegleDeCalcul> saveAll(List<RegleDeCalcul> regleDeCalculs) throws ElementAlreadyExistsException, InternalErrorException {
        List<RegleDeCalcul> createdRegleDeCalculs = new ArrayList<>(regleDeCalculs.size());

        regleDeCalculs.forEach(regleDeCalcul -> createdRegleDeCalculs.add(save(regleDeCalcul)));

        return createdRegleDeCalculs;
    }

    @Override
    public RegleDeCalcul findByCodeRegleDeCalcul(String codeRegleDeCalcul) throws ElementNotFoundException {
        return regleDeCalculRepository.findByCodeRegle(codeRegleDeCalcul)
                .orElseThrow(() -> regleDeCalculNotFoundException(codeRegleDeCalcul));
    }

    @Override
    public Page<RegleDeCalcul> findAll(Pageable pageable) {
        return regleDeCalculRepository.findAll(pageable);
    }

    @Override
    public RegleDeCalcul update(RegleDeCalcul regleDeCalcul) throws ElementNotFoundException, InternalErrorException {
        if (!regleDeCalculRepository.existsByCodeRegle(regleDeCalcul.getCodeRegle())) {
            throw regleDeCalculNotFoundException(regleDeCalcul.getCodeRegle());
        }

        RegleDeCalcul updatedRegleDeCalcul = null;

        try {
            updatedRegleDeCalcul = regleDeCalculRepository.save(regleDeCalcul);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedRegleDeCalcul;
    }

    @Override
    public void deleteByCodeRegleDeCalcul(String codeRegleDeCalcul) throws ElementNotFoundException, InternalErrorException {

        if (!regleDeCalculRepository.existsByCodeRegle(codeRegleDeCalcul)) {
            throw regleDeCalculNotFoundException(codeRegleDeCalcul);
        }

        regleDeCalculRepository.deleteByCodeRegle(codeRegleDeCalcul);
    }

    @Override
    @Transactional
    public void deleteAllByCodeRegleDeCalcul(List<String> codesRegleDeCalculs) throws ElementNotFoundException {
        codesRegleDeCalculs.forEach(this::deleteByCodeRegleDeCalcul);
    }

    private ElementNotFoundException regleDeCalculNotFoundException(String codeRegleDeCalcul) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.REGLE_DE_CALCUL_NOT_FOUND)
                .args(new Object[]{codeRegleDeCalcul})
                .build();
    }


}
