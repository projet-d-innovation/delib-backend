package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.exception.*;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.model.RegleDeCalcul;
import ma.enset.filiereservice.proxy.DepartementFeignClient;
import ma.enset.filiereservice.proxy.SemestreFeignClient;
import ma.enset.filiereservice.repository.FiliereRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FiliereServiceImpl implements FiliereService {
    FiliereRepository filiereRepository;
    RegleDeCalculServiceImpl regleDeCalculServiceImpl;

    DepartementFeignClient departementFeignClient;

    SemestreFeignClient semestreFeignClient;

    @Override
    @Transactional
    public Filiere create(Filiere filiere) throws FiliereAlreadyExistsException, RegleDeCalculNotFoundException, DepartementNotFoundException {

        Filiere createdFiliere = null;

        if (filiereRepository.existsById(filiere.getCodeFiliere()))
            throw FiliereAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.Filiere_ALREADY_EXISTS)
                    .args(new Object[]{"code", filiere.getCodeFiliere()})
                    .build();

        try {
            departementFeignClient.findById(filiere.getCodeDepartement());
        } catch (Exception e) {
            throw DepartementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.Departement_NOT_FOUND)
                    .args(new Object[]{"code", filiere.getCodeDepartement()})
                    .build();
        }

        RegleDeCalcul regleDeCalcul = regleDeCalculServiceImpl.findById(filiere.getCodeRegleDeCalcul());
        filiere.setRegleDeCalcul(regleDeCalcul);


        try {
            createdFiliere = filiereRepository.save(filiere);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code filiere", filiere.getCodeFiliere()})
                        .build();
            }
        }

        return createdFiliere;

    }

    @Override
    @Transactional
    public List<Filiere> createMany(List<Filiere> filieres) throws FiliereAlreadyExistsException, DepartementNotFoundException, RegleDeCalculNotFoundException {
        List<Filiere> createdFilieres = new ArrayList<>();

        for (Filiere filiere : filieres) {
            createdFilieres.add(create(filiere));
        }

        return createdFilieres;
    }

    @Override
    public Filiere findById(String code) throws FiliereNotFoundException {
        return filiereRepository.findById(code)
                .orElseThrow(() ->
                        FiliereNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.Filiere_NOT_FOUND)
                                .args(new Object[]{"code", code})
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
    @Transactional
    public Filiere update(Filiere filiere) throws FiliereNotFoundException, DepartementNotFoundException, RegleDeCalculNotFoundException {
        if (!filiereRepository.existsById(filiere.getCodeFiliere())) {
            throw FiliereNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.Filiere_NOT_FOUND)
                    .args(new Object[]{"code", filiere.getCodeFiliere()})
                    .build();
        }

        try {
            departementFeignClient.findById(filiere.getCodeDepartement());
        } catch (Exception e) {
            throw DepartementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.Departement_NOT_FOUND)
                    .args(new Object[]{"code Departement", filiere.getCodeDepartement()})
                    .build();
        }

        RegleDeCalcul regleDeCalcul = regleDeCalculServiceImpl.findById(filiere.getCodeRegleDeCalcul());
        filiere.setRegleDeCalcul(regleDeCalcul);


        Filiere updatedFiliere = null;

        try {
            updatedFiliere = filiereRepository.save(filiere);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code filiere", filiere.getCodeFiliere()})
                        .build();
            }
        }

        return updatedFiliere;
    }

    @Override
    @Transactional
    public List<Filiere> updateMany(List<Filiere> elements) throws BusinessException {
        List<Filiere> updatedFilieres = new ArrayList<>();

        for (Filiere filiere : elements) {
            updatedFilieres.add(update(filiere));
        }

        return updatedFilieres;
    }

    @Override
    public void deleteById(String code) throws FiliereNotFoundException, CannotDeleteFiliereException {
        Filiere filiere = findById(code);
        if (filiere.getSemestres() != null && filiere.getSemestres().size() > 0)
            throw CannotDeleteFiliereException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                    .args(new Object[]{"code Filiere", code})
                    .build();

        filiereRepository.deleteById(code);
    }

    @Override
    @Transactional
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
                    .key(CoreConstants.BusinessExceptionMessage.Departement_NOT_FOUND)
                    .args(new Object[]{"code Departement", codeDepartement})
                    .build();
        }
        return exists;

    }



}
