package ma.enset.departementservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.departementservice.constant.CoreConstants;
import ma.enset.departementservice.exception.ElementAlreadyExistsException;
import ma.enset.departementservice.exception.ElementNotFoundException;
import ma.enset.departementservice.exception.InternalErrorException;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.repository.DepartementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DepartementServiceImpl implements DepartementService {
    DepartementRepository departementRepository;

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
    public void deleteByCodeDepartement(String codeDepartement) throws ElementNotFoundException {
        if (!departementRepository.existsByCodeDepartement(codeDepartement)) {
            throw departementNotFoundException(codeDepartement);
        }

        departementRepository.deleteByCodeDepartement(codeDepartement);
    }

    @Override
    @Transactional
    public void deleteAllByCodeDepartement(List<String> codesDepartements) throws ElementNotFoundException {
        codesDepartements.forEach(this::deleteByCodeDepartement);
    }

    private ElementNotFoundException departementNotFoundException(String codeDepartement) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.DEPARTEMENT_NOT_FOUND)
                .args(new Object[]{codeDepartement})
                .build();
    }
}
