package ma.enset.departementservice.service;

import ma.enset.departementservice.exception.CannotDeleteDepartementException;
import ma.enset.departementservice.exception.DepartementAlreadyExistsException;
import ma.enset.departementservice.exception.DepartementNotFoundException;
import ma.enset.departementservice.model.Departement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartementService {
    Departement create(Departement departement) throws DepartementAlreadyExistsException;

    List<Departement> createMany(List<Departement> departements) throws DepartementAlreadyExistsException;

    Departement findById(String code) throws DepartementNotFoundException;

    List<Departement> findManyById(List<String> codes) throws DepartementNotFoundException;

    Page<Departement> findAll(Pageable pageable);

    Departement update(Departement department) throws DepartementNotFoundException;

    void deleteById(String code) throws DepartementNotFoundException , CannotDeleteDepartementException;

    void deleteManyById(List<String> codes) throws DepartementNotFoundException , CannotDeleteDepartementException;


}
