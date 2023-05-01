package ma.enset.filiereservice.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.proxy.AnnUnivFeignClient;
import ma.enset.filiereservice.proxy.DepartementFeignClient;
import ma.enset.filiereservice.proxy.SemestreFeignClient;
import ma.enset.filiereservice.repository.FiliereRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j

public class FiliereServiceImp implements FiliereService {
    FiliereRepository filiereRepository;

    DepartementFeignClient departementFeignClient;

    SemestreFeignClient semestreFeignClient;

    AnnUnivFeignClient annUnivFeignClient;

    @Override
    public Filiere save(Filiere filiere) throws ElementAlreadyExistsException, InternalErrorException, ElementNotFoundException {

        if (filiereRepository.existsByCodeFiliere(filiere.getCodeFiliere())) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.FILIERE_ALREADY_EXISTS)
                    .args(new Object[]{filiere.getCodeFiliere()})
                    .build();
        }
        try {
            departementFeignClient.existsByCodeDepartment(filiere.getCodeDepartement());
        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            System.out.println(exception.status());
            if (exception.status() == 404)
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_NOT_FOUND)
                        .args(new Object[]{filiere.getCodeDepartement()})
                        .build();
            else if (exception.status() == 500)
                throw new InternalErrorException();
        }
        ;

        try {

            departementFeignClient.isUserInDepartment(filiere.getCodeDepartement(), filiere.getCodeChefFiliere());


        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            if (exception.status() == 404)
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                        .args(new Object[]{filiere.getCodeChefFiliere()})
                        .build();
            throw new InternalErrorException();
        }

        try {
            departementFeignClient.addFiliereToDepartment(filiere.getCodeDepartement(), filiere.getCodeFiliere());
        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            if (exception.status() == 404)
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_NOT_FOUND)
                        .args(new Object[]{filiere.getCodeDepartement()})
                        .build();
            else if (exception.status() == 500)
                throw new InternalErrorException();
        }

        Filiere createdFiliere = null;

        try {
            createdFiliere = filiereRepository.save(filiere);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return createdFiliere;
    }

    @Override
    @Transactional
    public List<Filiere> saveAll(List<Filiere> filieres) throws ElementAlreadyExistsException, InternalErrorException {
        List<Filiere> createdFilieres = new ArrayList<>(filieres.size());

        filieres.forEach(filiere -> createdFilieres.add(save(filiere)));

        return createdFilieres;
    }

    @Override
    public Filiere findByCodeFiliere(String codeFiliere) throws ElementNotFoundException {
        return filiereRepository.findByCodeFiliere(codeFiliere)
                .orElseThrow(() -> filiereNotFoundException(codeFiliere));
    }

    @Override
    public List<Filiere> findByCodeRegle(String codeRegle) {
        return filiereRepository.findAllByCodeRegle(codeRegle);
    }

    @Override
    public Filiere findByCodeChefFiliere(String codeChefFiliere) throws ElementNotFoundException {
        return filiereRepository.findByCodeChefFiliere(codeChefFiliere)
                .orElseThrow(() -> filiereNotFoundException(codeChefFiliere));
    }

    @Override
    public List<Filiere> findByCodeDepartement(String codeDepartement) {
        return filiereRepository.findAllByCodeDepartement(codeDepartement);
    }

    @Override
    public Page<Filiere> findAll(Pageable pageable) {
        return filiereRepository.findAll(pageable);
    }


    @Override
    public void deleteByCodeFiliere(String codeFiliere) throws ElementNotFoundException, InternalErrorException {


        Filiere filiere = filiereRepository.findByCodeFiliere(codeFiliere).orElseThrow(() -> filiereNotFoundException(codeFiliere));

        if (filiere.getSemestreIds().size() > 0)
            throw new InternalErrorException();

        try {
            departementFeignClient.existsByCodeDepartment(filiere.getCodeDepartement());
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            if (e.status() == 404)
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_NOT_FOUND)
                        .args(new Object[]{filiere.getCodeDepartement()})
                        .build();

            throw new InternalErrorException();
        }
        try {
            departementFeignClient.deleteFiliereFromDepartment(filiere.getCodeDepartement(), codeFiliere);
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            if (e.status() == 404)
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND)
                        .args(new Object[]{codeFiliere})
                        .build();
            throw new InternalErrorException();
        }
        try {
            filiereRepository.deleteByCodeFiliere(codeFiliere);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
    }

    @Override
    @Transactional
    public void deleteAllByCodeFiliere(List<String> codesFilieres) throws ElementNotFoundException, InternalErrorException {
        codesFilieres.forEach(this::deleteByCodeFiliere);
    }

    @Override
    public Filiere addSemestreToFiliere(String codeFiliere, String codeSemestre) throws ElementNotFoundException, InternalErrorException, ElementAlreadyExistsException {
        Filiere foundedFiliere = filiereRepository.findByCodeFiliere(codeFiliere).orElseThrow(() -> filiereNotFoundException(codeFiliere));
        if (foundedFiliere.getSemestreIds().contains(codeSemestre)) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.SEMESTRE_ALREADY_EXISTS)
                    .args(new Object[]{codeSemestre})
                    .build();
        }

        foundedFiliere.getSemestreIds().add(codeSemestre);
        foundedFiliere.setNombreSemestres(foundedFiliere.getSemestreIds().size());
        return filiereRepository.save(foundedFiliere);
    }

    @Override
    public void deleteSemestreFromFiliere(String codeFiliere, String codeSemestre) throws ElementNotFoundException, InternalErrorException {
        Filiere foundedFiliere = filiereRepository.findByCodeFiliere(codeFiliere).orElseThrow(() -> filiereNotFoundException(codeFiliere));
        try {
            if (!semestreFeignClient.doesSemesterExist(codeSemestre) || !foundedFiliere.getSemestreIds().contains(codeSemestre))
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.SEMESTRE_NOT_FOUND)
                        .args(new Object[]{codeSemestre})
                        .build();


        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            throw new InternalErrorException();
        }
        foundedFiliere.getSemestreIds().remove(codeSemestre);
        foundedFiliere.setNombreSemestres(foundedFiliere.getSemestreIds().size());
        filiereRepository.save(foundedFiliere);
    }

    @Override
    public Filiere addAnneUnivToFiliere(String codeFiliere, String codeAnneUniv) {
        Filiere foundedFiliere = filiereRepository.findByCodeFiliere(codeFiliere).orElseThrow(() -> filiereNotFoundException(codeFiliere));
        try {
            if (!annUnivFeignClient.doesAnnUnivExist(codeAnneUniv))
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ANNEE_UNIVERSITAIRE_NOT_FOUND)
                        .args(new Object[]{codeAnneUniv})
                        .build();
        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            throw new InternalErrorException();
        }
        if (foundedFiliere.getAnneeUniversitaireIds().contains(codeAnneUniv)) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ANNEE_UNIVERSITAIRE_ALREADY_EXISTS)
                    .args(new Object[]{codeAnneUniv})
                    .build();
        }

        foundedFiliere.getSemestreIds().add(codeAnneUniv);
        foundedFiliere.setNombreEtudiants(foundedFiliere.getAnneeUniversitaireIds().size());
        return filiereRepository.save(foundedFiliere);
    }

    @Override
    public void deleteAnneUnivFromFiliere(String codeFiliere, String codeAnneUniv) throws ElementNotFoundException, InternalErrorException {
        Filiere foundedFiliere = filiereRepository.findByCodeFiliere(codeFiliere).orElseThrow(() -> filiereNotFoundException(codeFiliere));
        try {
            if (!annUnivFeignClient.doesAnnUnivExist(codeAnneUniv) || !foundedFiliere.getSemestreIds().contains(codeAnneUniv))
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ANNEE_UNIVERSITAIRE_NOT_FOUND)
                        .args(new Object[]{codeAnneUniv})
                        .build();


        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            throw new InternalErrorException();
        }
        foundedFiliere.getAnneeUniversitaireIds().remove(codeAnneUniv);
        foundedFiliere.setNombreEtudiants(foundedFiliere.getAnneeUniversitaireIds().size());
        filiereRepository.save(foundedFiliere);
    }

    @Override
    public void existByCodeFiliere(String codeFiliere) throws ElementNotFoundException {

        if (!filiereRepository.existsByCodeFiliere(codeFiliere))
            throw ElementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND)
                    .args(new Object[]{codeFiliere})
                    .build();
        return;

    }

    @Override
    public List<Filiere> findByCodeFiliereContaining(String codeFiliere) {
        return filiereRepository.findByCodeFiliereContaining(codeFiliere);
    }

    @Override
    public Filiere update(Filiere filiere) throws ElementNotFoundException, InternalErrorException {
        Filiere foundedFiliere = filiereRepository.findByCodeFiliere(filiere.getCodeFiliere())
                .orElseThrow(() -> filiereNotFoundException(filiere.getCodeFiliere()));


        try {
            departementFeignClient.existsByCodeDepartment(filiere.getCodeDepartement());
        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            if (exception.status() == 404)
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_NOT_FOUND)
                        .args(new Object[]{filiere.getCodeDepartement()})
                        .build();

            throw new InternalErrorException();
        }

        try {
            departementFeignClient.isUserInDepartment(filiere.getCodeDepartement(), filiere.getCodeChefFiliere());

        } catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            if (exception.status() == 404)
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                        .args(new Object[]{filiere.getCodeChefFiliere()})
                        .build();
            throw new InternalErrorException();
        }

        try {
            System.out.println(findByCodeFiliere(filiere.getCodeFiliere()));

            departementFeignClient.deleteFiliereFromDepartment(foundedFiliere.getCodeDepartement(), foundedFiliere.getCodeFiliere());
            departementFeignClient.addFiliereToDepartment(filiere.getCodeDepartement(), filiere.getCodeFiliere());
        }
        catch (FeignException exception) {
            log.error(exception.getMessage(), exception.getCause());
            throw new InternalErrorException();
        }
        foundedFiliere.setCodeChefFiliere(filiere.getCodeChefFiliere());
        foundedFiliere.setCodeDepartement(filiere.getCodeDepartement());
        foundedFiliere.setIntituleFiliere(filiere.getIntituleFiliere());
        return filiereRepository.save(foundedFiliere);
    }

    @Override
    public List<Filiere> retrieveAllFiliereByChef(String codeChefFiliere) throws ElementNotFoundException {
        return filiereRepository.findAllByCodeChefFiliere(codeChefFiliere).orElseThrow(() -> ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                .args(new Object[]{codeChefFiliere})
                .build());

    }

    @Override
    public boolean isThisUserAChef(String codeUser) {
        return retrieveAllFiliereByChef(codeUser).size() > 0;
    }


    private ElementNotFoundException filiereNotFoundException(String codeFiliere) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND)
                .args(new Object[]{codeFiliere})
                .build();
    }

}
