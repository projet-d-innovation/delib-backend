package ma.enset.departementservice.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.departementservice.constant.CoreConstants;
import ma.enset.departementservice.exception.ElementAlreadyExistsException;
import ma.enset.departementservice.exception.ElementNotFoundException;
import ma.enset.departementservice.exception.InternalErrorException;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.proxy.FiliereFeignClient;
import ma.enset.departementservice.proxy.UserFeignClient;
import ma.enset.departementservice.repository.DepartementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class DepartementServiceImpl implements DepartementService {
    DepartementRepository departementRepository;
    UserFeignClient userFeignClient;
    FiliereFeignClient filiereFeignClient;

    @Override
    public Departement save(Departement departement) throws ElementAlreadyExistsException, InternalErrorException {
        if (departementRepository.existsByCodeDepartement(departement.getCodeDepartement())) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_ALREADY_EXISTS)
                    .args(new Object[]{departement.getCodeDepartement()})
                    .build();
        }

        Departement createdDepartement = null;

        try {
            createdDepartement = departementRepository.save(departement);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return createdDepartement;
    }

    @Override
    @Transactional
    public List<Departement> saveAll(List<Departement> departements) throws ElementAlreadyExistsException, InternalErrorException {
        List<Departement> createdDepartements = new ArrayList<>(departements.size());

        departements.forEach(departement -> createdDepartements.add(save(departement)));

        return createdDepartements;
    }

    @Override
    public Departement findByCodeDepartement(String codeDepartement) throws ElementNotFoundException {
        return departementRepository.findByCodeDepartement(codeDepartement)
                .orElseThrow(() -> departementNotFoundException(codeDepartement));
    }

    @Override
    public Page<Departement> findAll(Pageable pageable) {
        return departementRepository.findAll(pageable);
    }

    @Override
    public Departement update(Departement departement) throws ElementNotFoundException, InternalErrorException {
        if (!departementRepository.existsByCodeDepartement(departement.getCodeDepartement())) {
            throw departementNotFoundException(departement.getCodeDepartement());
        }
        try {
            userFeignClient.findByCode(departement.getCodeChefDepartement());
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value())
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                        .args(new Object[]{departement.getCodeChefDepartement()})
                        .build();
            else
                throw new InternalErrorException();
        }

        try {
            if (!departement.getUsersIds().contains(departement.getCodeChefDepartement()))
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                        .args(new Object[]{departement.getCodeChefDepartement()})
                        .build();
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }


        Departement updatedDepartement = null;


        try {
            updatedDepartement = departementRepository.save(departement);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedDepartement;
    }

    @Override
    public void deleteByCodeDepartement(String codeDepartement) throws ElementNotFoundException, InternalErrorException {
        Departement departement = departementRepository.findByCodeDepartement(codeDepartement)
                .orElseThrow(() -> departementNotFoundException(codeDepartement));

        if (departement.getFilieresIds().size() > 0)
            throw new InternalErrorException();


        departementRepository.deleteByCodeDepartement(codeDepartement);
    }

    @Override
    @Transactional
    public void deleteAllByCodeDepartement(List<String> codesDepartements) throws ElementNotFoundException, InternalErrorException {
        codesDepartements.forEach(this::deleteByCodeDepartement);
    }


    @Override
    public Departement pushFiliereToDepartment(String codeDepartement, String codeFiliere) throws InternalErrorException, ElementNotFoundException, ElementAlreadyExistsException {


        Departement updatedDepartement = departementRepository.findByCodeDepartement(codeDepartement).orElseThrow(() -> departementNotFoundException(codeDepartement));

        List<String> filieresIds = updatedDepartement.getFilieresIds();
        if (!filieresIds.contains(codeFiliere)) {
            filieresIds.add(codeFiliere);
            updatedDepartement.setNombreFilieres(updatedDepartement.getFilieresIds().size());
            departementRepository.save(updatedDepartement);
        } else throw ElementAlreadyExistsException.builder()
                .key(CoreConstants.BusinessExceptionMessage.FILIERE_ALREADY_EXISTS)
                .args(new Object[]{codeFiliere})
                .build();
        return updatedDepartement;
    }

    @Override
    public void deleteFiliereFromDepartment(String codeFiliere, String codeDepartement) throws InternalErrorException, ElementNotFoundException {

        Departement updatedDepartement = departementRepository.findByCodeDepartement(codeDepartement)
                .orElseThrow(() -> departementNotFoundException(codeDepartement));
        try {
            filiereFeignClient.doesFiliereExist(codeFiliere);
        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());

            if (e.status() == HttpStatus.NOT_FOUND.value())
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND)
                        .args(new Object[]{codeFiliere})
                        .build();

            throw new InternalErrorException();
        }

        if (updatedDepartement.getFilieresIds().contains(codeFiliere)) {
            updatedDepartement.getFilieresIds().remove(codeFiliere);
            updatedDepartement.setNombreFilieres(updatedDepartement.getFilieresIds().size());
            departementRepository.save(updatedDepartement);
        } else throw ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND)
                .args(new Object[]{codeFiliere})
                .build();

    }

    @Override
    public Departement pushUserToDepartment(String codeDepartement, String codeUser) throws InternalErrorException, ElementNotFoundException, ElementAlreadyExistsException {

        Departement updatedDepartement = departementRepository.findByCodeDepartement(codeDepartement).orElseThrow(() -> departementNotFoundException(codeDepartement));


        List<String> userIds = updatedDepartement.getUsersIds();
        if (!userIds.contains(codeUser)) {
            userIds.add(codeUser);
            updatedDepartement.setNombreEmployes(updatedDepartement.getNombreEmployes() + 1);
            departementRepository.save(updatedDepartement);
        } else throw ElementAlreadyExistsException.builder()
                .key(CoreConstants.BusinessExceptionMessage.USER_ALREADY_EXISTS)
                .args(new Object[]{codeUser})
                .build();

        return updatedDepartement;
    }

    @Override
    @Transactional
    public void pushUsersToDepartment(String codeDepartement, List<String> codeUsers) throws InternalErrorException, ElementNotFoundException, ElementAlreadyExistsException {
        codeUsers.forEach(codeUser -> pushUserToDepartment(codeDepartement, codeUser));
    }


    @Override
    public void deleteUserFromDepartment(String codeUser, String codeDepartement) throws InternalErrorException, ElementNotFoundException {

        Departement updatedDepartement = departementRepository.findByCodeDepartement(codeDepartement).orElseThrow(() -> departementNotFoundException(codeDepartement));

        try {
            userFeignClient.findByCode(codeUser);

        } catch (FeignException e) {
            log.error(e.getMessage(), e.getCause());
            if (e.status() == HttpStatus.NOT_FOUND.value())
                throw ElementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                        .args(new Object[]{codeUser})
                        .build();
            else
                throw new InternalErrorException();
        }


        if (Objects.equals(codeUser, updatedDepartement.getCodeChefDepartement()) || filiereFeignClient.isThisUserAChef(codeUser))
            throw new InternalErrorException();


        if (updatedDepartement.getUsersIds().contains(codeUser)) {
            updatedDepartement.getUsersIds().remove(codeUser);
            updatedDepartement.setNombreEmployes(updatedDepartement.getNombreEmployes() - 1);
            departementRepository.save(updatedDepartement);
        } else throw ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                .args(new Object[]{codeUser})
                .build();
    }

    @Override
    public void isUserInDepartment(String codeUser, String codeDepartement) throws ElementNotFoundException {
        if (!departementRepository.findByCodeDepartement(codeDepartement).orElseThrow(() -> departementNotFoundException(codeDepartement)).getUsersIds().contains(codeUser)) {
            throw ElementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.USER_NOT_FOUND)
                    .args(new Object[]{codeUser})
                    .build();
        }
        ;
    }

    @Override
    public List<Departement> findByCodeDepartementContaining(String codeDepartement) {
        return departementRepository.findByCodeDepartementContaining(codeDepartement);
    }

    @Override
    public void existByCodeDepartement(String codeDepartement) {
        if (!departementRepository.existsByCodeDepartement(codeDepartement))
            throw ElementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_NOT_FOUND)
                    .args(new Object[]{codeDepartement})
                    .build();
    }


    private ElementNotFoundException departementNotFoundException(String codeDepartement) throws ElementNotFoundException {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_NOT_FOUND)
                .args(new Object[]{codeDepartement})
                .build();
    }
}
