package ma.enset.semestreservice.service;

import lombok.AllArgsConstructor;
import ma.enset.semestreservice.constant.CoreConstants;
import ma.enset.semestreservice.exception.FiliereNotFoundException;
import ma.enset.semestreservice.exception.SemestreAlreadyExistsException;
import ma.enset.semestreservice.exception.SemestreNotFoundException;
import ma.enset.semestreservice.model.Semestre;
import ma.enset.semestreservice.proxy.FiliereFeignClient;
import ma.enset.semestreservice.repository.SemestreRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SemestreServiceImpl implements SemestreService{
    SemestreRepository semestreRepository;

    FiliereFeignClient filiereFeignClient;


    @Override
    public Semestre create(Semestre semestre) throws SemestreAlreadyExistsException, FiliereNotFoundException {

        Semestre createdSemestre = null;


        if (semestreRepository.existsById(semestre.getCodeSemestre()))
            throw SemestreAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{"code semestre", semestre.getCodeSemestre()})
                    .build();

        // TODO : trigger the exception from the other Filiere service

        try {
            filiereFeignClient.findById(semestre.getCodeFiliere());
        } catch (Exception e) {
            throw FiliereNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Filiere", semestre.getCodeFiliere()})
                    .build();
        }


        try {
            createdSemestre = semestreRepository.save(semestre);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw SemestreAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code semestre", semestre.getCodeSemestre()})
                        .build();
            }
        }

        return createdSemestre;

    }

    @Override
    public List<Semestre> createMany(List<Semestre> semestres) throws SemestreAlreadyExistsException , FiliereNotFoundException {
        List<Semestre> createdSemestres = new ArrayList<>();

        for (Semestre semestre : semestres) {
            // TODO : trigger the exception from the other departement service

            try {
                filiereFeignClient.findById(semestre.getCodeFiliere());
            } catch (Exception e) {
                throw FiliereNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                        .args(new Object[]{"code Filiere", semestre.getCodeFiliere()})
                        .build();
            }

            if (semestreRepository.existsById(semestre.getCodeSemestre()))
                throw SemestreAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code Semestre", semestre.getCodeSemestre()})
                        .build();



            try {
                createdSemestres.add(semestreRepository.save(semestre));
            } catch (Exception e) {
                if (e.getCause() instanceof ConstraintViolationException) {
                    throw SemestreAlreadyExistsException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                            .args(new Object[]{"code Semestre", semestre.getCodeSemestre()})
                            .build();
                }
            }
        }

        return createdSemestres;
    }

    @Override
    public Semestre findById(String code) throws SemestreNotFoundException {
        return semestreRepository.findById(code)
                .orElseThrow(() ->
                        SemestreNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                                .args(new Object[]{"code semestre", code})
                                .build()
                );
    }

    @Override
    public List<Semestre> findManyById(List<String> codes) throws SemestreNotFoundException {
        List<Semestre> semestres = new ArrayList<>();
        for (String codeElement : codes) {
            semestres.add(findById(codeElement));
        }
        return semestres;
    }

    @Override
    public Page<Semestre> findAll(Pageable pageable) {
        return semestreRepository.findAll(pageable);
    }

    @Override
    public Semestre update(Semestre semestre) throws SemestreNotFoundException, FiliereNotFoundException {
        if (!semestreRepository.existsById(semestre.getCodeSemestre())) {
            throw SemestreNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code semestre", semestre.getCodeSemestre()})
                    .build();
        }
        // TODO : trigger the exception from the other departement service

        try {
            filiereFeignClient.findById(semestre.getCodeFiliere());
        } catch (Exception e) {
            throw FiliereNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Filiere", semestre.getCodeFiliere()})
                    .build();
        }



        Semestre updatedSemestre = null;

        try {
            updatedSemestre = semestreRepository.save(semestre);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw SemestreAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code semestre", semestre.getCodeSemestre()})
                        .build();
            }
        }

        return updatedSemestre;
    }

    @Override
    public void deleteById(String code) throws SemestreNotFoundException {

        // TODO : Check Module and Session
        if (!semestreRepository.existsById(code)) {
            throw SemestreNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code semestre", code})
                    .build();
        }

        semestreRepository.deleteById(code);
    }

    @Override
    public void deleteManyById(List<String> codes) throws SemestreNotFoundException {
        for (String code : codes) {
            this.deleteById(code);
        }
    }

    public Boolean existsByCodeFiliere(String codeDepartement) throws FiliereNotFoundException {
        boolean exists = false;
        try {
            exists = semestreRepository.existsByCodeFiliere(codeDepartement);
        }
        catch (Exception e){
            throw FiliereNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code Filiere", codeDepartement})
                    .build();
        }
        return exists;

    }
}
