package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.exception.CannotDeleteRegleDeCalculException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.exception.RegleDeCalculAlreadyExistsException;
import ma.enset.filiereservice.exception.RegleDeCalculNotFoundException;
import ma.enset.filiereservice.model.RegleDeCalcul;
import ma.enset.filiereservice.repository.FiliereRepository;
import ma.enset.filiereservice.repository.RegleDeCalculRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RegleDeCalculServiceImpl implements RegleDeCalculService {
    RegleDeCalculRepository regleDeCalculRepository;

    FiliereRepository filiereRepository;

    @Override
    public RegleDeCalcul create(RegleDeCalcul RegleDeCalcul) throws RegleDeCalculAlreadyExistsException {

        RegleDeCalcul createdRegleDeCalcul = null;

        if (regleDeCalculRepository.existsById(RegleDeCalcul.getCodeRegle()))
            throw RegleDeCalculAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.RegleDeCalcule_ALREADY_EXISTS)
                    .args(new Object[]{"code ", RegleDeCalcul.getCodeRegle()})
                    .build();

        try {
            createdRegleDeCalcul = regleDeCalculRepository.save(RegleDeCalcul);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code RegleDeCalcul", RegleDeCalcul.getCodeRegle()})
                        .build();
            }
        }

        return createdRegleDeCalcul;

    }

    @Transactional
    @Override
    public List<RegleDeCalcul> createMany(List<RegleDeCalcul> RegleDeCalculs) throws RegleDeCalculAlreadyExistsException {
        List<RegleDeCalcul> createdRegleDeCalculs = new ArrayList<>();

        RegleDeCalculs.forEach(regleDeCalcul -> createdRegleDeCalculs.add(create(regleDeCalcul)));

        return createdRegleDeCalculs;
    }

    @Override
    public RegleDeCalcul findById(String code) throws RegleDeCalculNotFoundException {
        return regleDeCalculRepository.findById(code)
                .orElseThrow(() ->
                        RegleDeCalculNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.RegleDeCalcul_NOT_FOUND)
                                .args(new Object[]{"code ", code})
                                .build()
                );
    }

    @Override
    public List<RegleDeCalcul> findManyById(List<String> codes) throws RegleDeCalculNotFoundException {
        List<RegleDeCalcul> RegleDeCalculs = new ArrayList<>();
        for (String codeElement : codes) {
            RegleDeCalculs.add(findById(codeElement));
        }
        return RegleDeCalculs;
    }

    @Override
    public Page<RegleDeCalcul> findAll(Pageable pageable) {
        return regleDeCalculRepository.findAll(pageable);
    }

    @Override
    public RegleDeCalcul update(RegleDeCalcul RegleDeCalcul) throws RegleDeCalculNotFoundException {
        if (!regleDeCalculRepository.existsById(RegleDeCalcul.getCodeRegle())) {
            throw RegleDeCalculNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.RegleDeCalcul_NOT_FOUND)
                    .args(new Object[]{"code ", RegleDeCalcul.getCodeRegle()})
                    .build();
        }

        RegleDeCalcul updatedRegleDeCalcul = null;

        try {
            updatedRegleDeCalcul = regleDeCalculRepository.save(RegleDeCalcul);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code RegleDeCalcul", RegleDeCalcul.getCodeRegle()})
                        .build();
            }
        }

        return updatedRegleDeCalcul;
    }
    @Transactional
    @Override
    public List<RegleDeCalcul> updateMany(List<RegleDeCalcul> RegleDeCalculs) throws RegleDeCalculNotFoundException {
        List<RegleDeCalcul> updatedRegleDeCalculs = new ArrayList<>();

        RegleDeCalculs.forEach(regleDeCalcul -> updatedRegleDeCalculs.add(update(regleDeCalcul)));

        return updatedRegleDeCalculs;
    }

    @Override
    public void deleteById(String code) throws RegleDeCalculNotFoundException {

        RegleDeCalcul regleDeCalcul = this.findById(code);

        if (filiereRepository.existsByRegleDeCalcul(regleDeCalcul))
            throw CannotDeleteRegleDeCalculException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.RegleDeCalcul_CannotBeDeleted)
                    .args(new Object[]{"code ", code})
                    .build();

        regleDeCalculRepository.deleteById(code);
    }

    @Override
    @Transactional
    public void deleteManyById(List<String> codes) throws RegleDeCalculNotFoundException {
        for (String code : codes) {
            deleteById(code);
        }
    }
}
