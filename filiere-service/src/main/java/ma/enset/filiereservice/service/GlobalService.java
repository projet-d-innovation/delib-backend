package ma.enset.filiereservice.service;

import ma.enset.filiereservice.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GlobalService<T> {
    T create(T element) throws BusinessException;

    List<T> createMany(List<T> elements) throws BusinessException;

    T findById(String code) throws BusinessException;

    List<T> findManyById(List<String> codes) throws BusinessException;

    Page<T> findAll(Pageable pageable);

    T update(T element) throws BusinessException;

    void deleteById(String code) throws BusinessException;

    void deleteManyById(List<String> codes) throws BusinessException;


}
