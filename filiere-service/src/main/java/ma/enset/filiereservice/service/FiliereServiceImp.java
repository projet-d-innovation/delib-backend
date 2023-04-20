package ma.enset.filiereservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.repository.FiliereRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
@Slf4j

public class FiliereServiceImp implements FiliereService{
    FiliereRepository filiereRepository;
    @Override
    public Filiere save(Filiere filiere) throws ElementAlreadyExistsException, InternalErrorException {
        if (filiereRepository.existsByCodeFiliere(filiere.getCodeFiliere())) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.FILIERE_ALREADY_EXISTS)
                    .args(new Object[]{filiere.getCodeFiliere()})
                    .build();
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
    public List<Filiere> findByCodeRegle(String codeRegle)  {
        return filiereRepository.findAllByCodeRegle(codeRegle);
    }

    @Override
    public Filiere findByCodeChefFiliere(String codeChefFiliere)  {
        System.out.println("codeChefFiliere = " + codeChefFiliere);
        return filiereRepository.findByCodeChefFiliere(codeChefFiliere);
    }

    @Override
    public List<Filiere> findByCodeDepartement(String codeDepartement)  {
        return filiereRepository.findAllByCodeDepartement(codeDepartement);
    }

    @Override
    public Page<Filiere> findAll(Pageable pageable) {
        return filiereRepository.findAll(pageable);
    }

    @Override
    public Filiere update(Filiere filiere) throws ElementNotFoundException, InternalErrorException {
        if (!filiereRepository.existsByCodeFiliere(filiere.getCodeFiliere())) {
            throw filiereNotFoundException(filiere.getCodeFiliere());
        }

        Filiere updatedFiliere = null;

        try {
            updatedFiliere = filiereRepository.save(filiere);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedFiliere;
    }

    @Override
    public void deleteByCodeFiliere(String codeFiliere) throws ElementNotFoundException, InternalErrorException {

        if (!filiereRepository.existsByCodeFiliere(codeFiliere)) {
            throw filiereNotFoundException(codeFiliere);
        }

        filiereRepository.deleteByCodeFiliere(codeFiliere);
    }

    @Override
    @Transactional
    public void deleteAllByCodeFiliere(List<String> codesFilieres) throws ElementNotFoundException {
        codesFilieres.forEach(this::deleteByCodeFiliere);
    }

    private ElementNotFoundException filiereNotFoundException(String codeFiliere) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.FILIERE_NOT_FOUND)
                .args(new Object[]{codeFiliere})
                .build();
    }

}
