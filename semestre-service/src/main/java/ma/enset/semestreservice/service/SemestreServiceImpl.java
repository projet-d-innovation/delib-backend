package ma.enset.semestreservice.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.semestreservice.constant.CoreConstants;
import ma.enset.semestreservice.exception.ElementAlreadyExistsException;
import ma.enset.semestreservice.exception.ElementNotFoundException;
import ma.enset.semestreservice.exception.InternalErrorException;
import ma.enset.semestreservice.model.Semestre;
import ma.enset.semestreservice.proxy.FiliereFeignClient;
import ma.enset.semestreservice.proxy.ModuleFeignClient;
import ma.enset.semestreservice.proxy.SessionFeignClient;
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
public class SemestreServiceImpl implements SemestreService {
    SemestreRepository semestreRepository;

    FiliereFeignClient filiereFeignClient;

    ModuleFeignClient moduleFeignClient;

    SessionFeignClient sessionFeignClient;

    @Override
    public Semestre save(Semestre semestre) throws ElementAlreadyExistsException, InternalErrorException {
        if (semestreRepository.existsByCodeSemestre(semestre.getCodeSemestre()))
            throw elementAlreadyExistsException(semestre.getCodeSemestre(),
                    CoreConstants.BusinessExceptionMessage.SEMESTRE_ALREADY_EXISTS);

        try {
            filiereFeignClient.doesFiliereExist(semestre.getCodeFiliere());
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            if (e.status() == 404)
                throw elementNotFoundException(semestre.getCodeFiliere(),
                        CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND);
            else
                throw new InternalErrorException();
        }

        try {
            filiereFeignClient.addSemestreToFiliere(semestre.getCodeFiliere(), semestre.getCodeSemestre());
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            if (e.status() == 404)
                throw elementNotFoundException(semestre.getCodeFiliere(),
                        CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND);

            else if (e.status() == 500)
                throw new InternalErrorException();

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
                .orElseThrow(() -> elementNotFoundException(codeSemestre,
                        CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND));
    }

    @Override
    public Page<Semestre> findAll(Pageable pageable) {
        return semestreRepository.findAll(pageable);
    }

    @Override
    public Semestre update(Semestre semestre) throws ElementNotFoundException, InternalErrorException {
        if (!semestreRepository.existsByCodeSemestre(semestre.getCodeSemestre())) {
            throw elementNotFoundException(semestre.getCodeSemestre(),
                    CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND);
        }
        try {
            filiereFeignClient.doesFiliereExist(semestre.getCodeFiliere());
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            if (e.status() == 404)
                throw elementNotFoundException(semestre.getCodeFiliere(),
                        CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND);
            else
                throw new InternalErrorException();
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
        Semestre foundedSemester = semestreRepository.findByCodeSemestre(codeSemestre)
                .orElseThrow(() -> elementNotFoundException(codeSemestre,
                        CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND));

        if (foundedSemester.getSessionsIds().size() > 0 || foundedSemester.getModulesIds().size() > 0)
            throw new InternalErrorException();

        try {
            filiereFeignClient.removeSemestreFromFiliere(foundedSemester.getCodeFiliere(), foundedSemester.getCodeSemestre());
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            if (e.status() == 404)
                throw elementNotFoundException(foundedSemester.getCodeFiliere(),
                        CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND);
            else
                throw new InternalErrorException();

        }

        semestreRepository.deleteByCodeSemestre(codeSemestre);
    }

    @Override
    @Transactional
    public void deleteAllByCodeSemestre(List<String> codesSemestres) throws ElementNotFoundException {
        codesSemestres.forEach(this::deleteByCodeSemestre);
    }

    @Override
    public boolean doesSemesterExist(String codeSemestre) {
        return semestreRepository.existsByCodeSemestre(codeSemestre);
    }

    @Override
    public Semestre addModuleToSemestre(String codeSemestre, String codeModule) throws ElementNotFoundException, InternalErrorException {
        Semestre foundedSemestre = semestreRepository.findByCodeSemestre(codeSemestre)
                .orElseThrow(() -> elementNotFoundException(codeSemestre,
                        CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND));

        if (foundedSemestre.getModulesIds().contains(codeModule))
            throw elementAlreadyExistsException(codeSemestre,
                    CoreConstants.BusinessExceptionMessage.MODULE_ALREADY_EXISTS);
        else {
            foundedSemestre.getModulesIds().add(codeModule);
            foundedSemestre.setNombreModules(foundedSemestre.getModulesIds().size());
            return semestreRepository.save(foundedSemestre);
        }

    }

    @Override
    public void removeModuleFromSemestre(String codeSemestre, String codeModule) throws ElementNotFoundException, InternalErrorException {
        Semestre foundedSemestre = semestreRepository.findByCodeSemestre(codeSemestre).orElseThrow(() -> elementNotFoundException(codeSemestre,
                CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND));
        try {
            moduleFeignClient.findByCode(codeModule);
            if (!foundedSemestre.getModulesIds().contains(codeModule))
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.MODULE_NOT_FOUND)
                        .args(new Object[]{codeModule})
                        .build();

        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            if (exception.status() == 404)
                throw elementNotFoundException(codeModule,
                        CoreConstants.BusinessExceptionMessage.MODULE_NOT_FOUND);
            else
                throw new InternalErrorException();
        }
        foundedSemestre.getModulesIds().remove(codeModule);
        foundedSemestre.setNombreModules(foundedSemestre.getModulesIds().size());
        semestreRepository.save(foundedSemestre);

    }

    @Override
    public Semestre addSessionToSemestre(String codeSemestre, String codeSession) throws ElementNotFoundException, InternalErrorException {

        Semestre foundedSemestre = semestreRepository.findByCodeSemestre(codeSemestre)
                .orElseThrow(() -> elementNotFoundException(codeSemestre,
                        CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND));

        if (foundedSemestre.getSessionsIds().contains(codeSession))
            throw elementAlreadyExistsException(codeSemestre,
                    CoreConstants.BusinessExceptionMessage.SESSION_ALREADY_EXISTS);
        else {
            foundedSemestre.getSessionsIds().add(codeSession);
            foundedSemestre.setNombreSessions(foundedSemestre.getSessionsIds().size());
            return semestreRepository.save(foundedSemestre);
        }
    }

    @Override
    public void removeSessionFromSemestre(String codeSemestre, String codeSession) throws ElementNotFoundException, InternalErrorException {
        Semestre foundedSemestre = semestreRepository.findByCodeSemestre(codeSemestre).orElseThrow(() -> elementNotFoundException(codeSemestre,
                CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND));
        try {
            sessionFeignClient.findByCode(codeSession);

            if (!foundedSemestre.getSessionsIds().contains(codeSession))
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.SESSION_NOT_FOUND)
                        .args(new Object[]{codeSession})
                        .build();


        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            if (exception.status() == 404)
                throw elementNotFoundException(codeSession,
                        CoreConstants.BusinessExceptionMessage.SESSION_NOT_FOUND);
            else
                throw new InternalErrorException();
        }
        foundedSemestre.getSessionsIds().remove(codeSession);
        foundedSemestre.setNombreSessions(foundedSemestre.getSessionsIds().size());
        semestreRepository.save(foundedSemestre);
    }

    private ElementNotFoundException elementNotFoundException(String codeSemestre, String key) {
        return ElementNotFoundException.builder()
                .key(key)
                .args(new Object[]{codeSemestre})
                .build();
    }

    private ElementAlreadyExistsException elementAlreadyExistsException(String codeSemestre, String key) {
        return ElementAlreadyExistsException.builder()
                .key(key)
                .args(new Object[]{codeSemestre})
                .build();
    }
}
