package ma.enset.departementservice.service;

import lombok.AllArgsConstructor;
import ma.enset.departementservice.constant.CoreConstants;
import ma.enset.departementservice.exception.CannotDeleteDepartementException;
import ma.enset.departementservice.exception.DepartementAlreadyExistsException;
import ma.enset.departementservice.exception.DepartementNotFoundException;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.proxy.FiliereFeignClient;
import ma.enset.departementservice.repository.DepartementRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DepartementServiceImpl implements DepartementService {
    DepartementRepository departementRepository;

    FiliereFeignClient filiereFeignClient;

    @Override
    public Departement create(Departement departement) throws DepartementAlreadyExistsException {

        Departement createdDepartement = null;

        if (departementRepository.existsById(departement.getCodeDepartement()))
            throw DepartementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{"code Departement", departement.getCodeDepartement()})
                    .build();

        try {
            createdDepartement = departementRepository.save(departement);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw DepartementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code Departement", departement.getCodeDepartement()})
                        .build();
            }
        }

        return createdDepartement;

    }

    @Override
    @Transactional
    public List<Departement> createMany(List<Departement> departements) throws DepartementAlreadyExistsException {
        List<Departement> createdDepartements = new ArrayList<>();

        for (Departement departement : departements) {
            if (departementRepository.existsById(departement.getCodeDepartement()))
                throw DepartementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code Departement", departement.getCodeDepartement()})
                        .build();

            try {
                createdDepartements.add(departementRepository.save(departement));
            } catch (Exception e) {
                if (e.getCause() instanceof ConstraintViolationException) {
                    throw DepartementAlreadyExistsException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                            .args(new Object[]{"code Departement", departement.getCodeDepartement()})
                            .build();
                }
            }
        }

        return createdDepartements;
    }

    @Override
    public Departement findById(String code) throws DepartementNotFoundException {
        return departementRepository.findById(code)
                .orElseThrow(() ->
                        DepartementNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                                .args(new Object[]{"code Departement", code})
                                .build()
                );
    }

    @Override
    public List<Departement> findManyById(List<String> codes) throws DepartementNotFoundException {
        List<Departement> departements = new ArrayList<>();
        for (String codeElement : codes) {
            departements.add(findById(codeElement));
        }
        return departements;
    }

    @Override
    public Page<Departement> findAll(Pageable pageable) {
        return departementRepository.findAll(pageable);
    }

    @Override
    public Departement update(Departement departement) throws DepartementNotFoundException {
        if (!departementRepository.existsById(departement.getCodeDepartement())) {
            throw DepartementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Departement", departement.getCodeDepartement()})
                    .build();
        }

        Departement updatedDepartement = null;

        try {
            updatedDepartement = departementRepository.save(departement);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw DepartementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code Departement", departement.getCodeDepartement()})
                        .build();
            }
        }

        return updatedDepartement;
    }

    @Override
    @Transactional
    public List<Departement> updateMany(List<Departement> departements) throws DepartementNotFoundException {
        List<Departement> updatedDepartements = new ArrayList();

        departements.forEach(departement -> {
            updatedDepartements.add(update(departement));
        });
        return updatedDepartements;
    }

    @Override
    public void deleteById(String code) throws DepartementNotFoundException, CannotDeleteDepartementException {

        //TODO: check if the user is using the departement

        try {
            boolean exists = filiereFeignClient.existsByCodeDepartement(code).getBody();
            if (exists)
                throw new Exception();
        } catch (Exception e) {
            throw CannotDeleteDepartementException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_USED_IN_ANOTHER_ENTITY)
                    .args(new Object[]{"code Departement", code})
                    .build();
        }

        if (!departementRepository.existsById(code)) {
            throw DepartementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Departement", code})
                    .build();
        }


        departementRepository.deleteById(code);
    }

    @Override
    @Transactional
    public void deleteManyById(List<String> codes) throws DepartementNotFoundException {
        codes.forEach(code -> deleteById(code));
    }
}
