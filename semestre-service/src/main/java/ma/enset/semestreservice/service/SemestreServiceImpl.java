package ma.enset.semestreservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.semestreservice.constant.CoreConstants;
import ma.enset.semestreservice.exception.ElementAlreadyExistsException;
import ma.enset.semestreservice.exception.ElementNotFoundException;
import ma.enset.semestreservice.exception.InternalErrorException;
import ma.enset.semestreservice.model.Semestre;
import ma.enset.semestreservice.repository.SemestreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SemestreServiceImpl implements SemestreService{
    SemestreRepository semestreRepository;

    @Override
    public Semestre save(Semestre semestre) throws ElementAlreadyExistsException, InternalErrorException {
        if (semestreRepository.existsByCodeSemestre(semestre.getCodeSemestre())) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.SEMESTRE_ALREADY_EXISTS)
                    .args(new Object[]{semestre.getCodeSemestre()})
                    .build();
        }

        Semestre createdSemestre = null;

        try {
            createdSemestre = semestreRepository.save(semestre);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return createdSemestre;
    }

    @Override
    @Transactional
    public List<Semestre> saveAll(List<Semestre> semestres) throws ElementAlreadyExistsException, InternalErrorException {
        List<Semestre> createdSemestres = new ArrayList<>(semestres.size());

        semestres.forEach(semestre -> createdSemestres.add(save(semestre)));

        return createdSemestres;
    }

    @Override
    public Semestre findByCodeSemestre(String codeSemestre) throws ElementNotFoundException {
        return semestreRepository.findByCodeSemestre(codeSemestre)
                .orElseThrow(() -> semestreNotFoundException(codeSemestre));
    }

    @Override
    public Page<Semestre> findAll(Pageable pageable) {
        return semestreRepository.findAll(pageable);
    }

    @Override
    public Semestre update(Semestre semestre) throws ElementNotFoundException, InternalErrorException {
        if (!semestreRepository.existsByCodeSemestre(semestre.getCodeSemestre())) {
            throw semestreNotFoundException(semestre.getCodeSemestre());
        }

        Semestre updatedSemestre = null;

        try {
            updatedSemestre = semestreRepository.save(semestre);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedSemestre;
    }

    @Override
    public void deleteByCodeSemestre(String codeSemestre) throws ElementNotFoundException {
        if (!semestreRepository.existsByCodeSemestre(codeSemestre)) {
            throw semestreNotFoundException(codeSemestre);
        }

        semestreRepository.deleteByCodeSemestre(codeSemestre);
    }

    @Override
    @Transactional
    public void deleteAllByCodeSemestre(List<String> codesSemestres) throws ElementNotFoundException {
        codesSemestres.forEach(this::deleteByCodeSemestre);
    }

    @Override
    public List<Semestre> findAllByCodesOfSemestres(List<String> codesSemestres) throws ElementNotFoundException {
        List<Semestre> semestres = new ArrayList<>();
        codesSemestres.forEach(code -> semestres.add(findByCodeSemestre(code)));
        return semestres;
    }

    private ElementNotFoundException semestreNotFoundException(String codeSemestre) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND)
                .args(new Object[]{codeSemestre})
                .build();
    }
}
