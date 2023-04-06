package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.exception.*;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.proxy.DepartementFeignClient;
import ma.enset.filiereservice.proxy.SemestreFeignClient;
import ma.enset.filiereservice.repository.FiliereRepository;
import ma.enset.filiereservice.repository.RegleDeCalculRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FiliereService implements GlobalService<Filiere> {
    FiliereRepository filiereRepository;
    RegleDeCalculRepository regleDeCalculRepository;

    DepartementFeignClient departementFeignClient;

    SemestreFeignClient semestreFeignClient;

    @Override
    public Filiere create(Filiere filiere) throws FiliereAlreadyExistsException, RegleDeCalculNotFoundException, DepartementNotFoundException {

        Filiere createdFiliere = null;


        if (filiereRepository.existsById(filiere.getCodeFiliere()))
            throw FiliereAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{"code filiere", filiere.getCodeFiliere()})
                    .build();
        if (!regleDeCalculRepository.existsById(filiere.getCodeRegle()))
            throw RegleDeCalculNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code RegledeCalcul", filiere.getCodeRegle()})
                    .build();
        // TODO : trigger the exception from the other departement service

        try {
            departementFeignClient.findById(filiere.getCodeDepartement());
        } catch (Exception e) {
            throw DepartementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Departement", filiere.getCodeDepartement()})
                    .build();
        }


        try {
            createdFiliere = filiereRepository.save(filiere);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw FiliereAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code filiere", filiere.getCodeFiliere()})
                        .build();
            }
        }

        return createdFiliere;

    }

    @Override
    public List<Filiere> createMany(List<Filiere> filieres) throws FiliereAlreadyExistsException, DepartementNotFoundException, RegleDeCalculNotFoundException {
        List<Filiere> createdFilieres = new ArrayList<>();

        for (Filiere filiere : filieres) {
            // TODO : trigger the exception from the other departement service

            try {
                departementFeignClient.findById(filiere.getCodeDepartement());
            } catch (Exception e) {
                throw DepartementNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                        .args(new Object[]{"code Departement", filiere.getCodeDepartement()})
                        .build();
            }

            if (filiereRepository.existsById(filiere.getCodeFiliere()))
                throw FiliereAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code Filiere", filiere.getCodeFiliere()})
                        .build();
            if (!regleDeCalculRepository.existsById(filiere.getCodeRegle()))
                throw RegleDeCalculNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                        .args(new Object[]{"code RegledeCalcul", filiere.getCodeRegle()})
                        .build();


            try {
                createdFilieres.add(filiereRepository.save(filiere));
            } catch (Exception e) {
                if (e.getCause() instanceof ConstraintViolationException) {
                    throw FiliereAlreadyExistsException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                            .args(new Object[]{"code Filiere", filiere.getCodeFiliere()})
                            .build();
                }
            }
        }

        return createdFilieres;
    }

    @Override
    public Filiere findById(String code) throws FiliereNotFoundException {
        return filiereRepository.findById(code)
                .orElseThrow(() ->
                        FiliereNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                                .args(new Object[]{"code filiere", code})
                                .build()
                );
    }

    @Override
    public List<Filiere> findManyById(List<String> codes) throws FiliereNotFoundException {
        List<Filiere> filieres = new ArrayList<>();
        for (String codeElement : codes) {
            filieres.add(findById(codeElement));
        }
        return filieres;
    }

    @Override
    public Page<Filiere> findAll(Pageable pageable) {
        return filiereRepository.findAll(pageable);
    }

    @Override
    public Filiere update(Filiere filiere) throws FiliereNotFoundException, DepartementNotFoundException, RegleDeCalculNotFoundException {
        if (!filiereRepository.existsById(filiere.getCodeFiliere())) {
            throw FiliereNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code filiere", filiere.getCodeFiliere()})
                    .build();
        }
        // TODO : trigger the exception from the other departement service

        try {
            departementFeignClient.findById(filiere.getCodeDepartement());
        } catch (Exception e) {
            throw DepartementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Departement", filiere.getCodeDepartement()})
                    .build();
        }

        if (!regleDeCalculRepository.existsById(filiere.getCodeRegle()))
            throw RegleDeCalculNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code RegledeCalcul", filiere.getCodeRegle()})
                    .build();


        Filiere updatedFiliere = null;

        try {
            updatedFiliere = filiereRepository.save(filiere);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw FiliereAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code filiere", filiere.getCodeFiliere()})
                        .build();
            }
        }

        return updatedFiliere;
    }

    @Override
    public void deleteById(String code) throws FiliereNotFoundException, CannotDeleteFiliereException {
        if (!filiereRepository.existsById(code)) {
            throw FiliereNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code filiere", code})
                    .build();
        }


        try {
            boolean exists = semestreFeignClient.existsByCodeFiliere(code).getBody();
            if (exists)
                throw CannotDeleteFiliereException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code Filiere", code})
                        .build();

        } catch (Exception e) {
            throw CannotDeleteFiliereException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                    .args(new Object[]{"code Filiere", code})
                    .build();
        }

        filiereRepository.deleteById(code);
    }

    @Override
    public void deleteManyById(List<String> codes) throws FiliereNotFoundException {
        for (String code : codes) {
            this.deleteById(code);
        }
    }

    public Boolean existsByCodeDepartement(String codeDepartement) throws DepartementNotFoundException {
        boolean exists = false;
        try {
            exists = filiereRepository.existsByCodeDepartement(codeDepartement);
        } catch (Exception e) {
            throw DepartementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Departement", codeDepartement})
                    .build();
        }
        return exists;

    }
}
