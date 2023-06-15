package ma.enset.inscriptionpedagogique.service;

import ma.enset.inscriptionpedagogique.dto.EtudiantResponse;
import ma.enset.inscriptionpedagogique.dto.RequiredSearchParams;

import java.util.List;

public interface EtudiantService {
    List<EtudiantResponse> findAllBySearchParams(RequiredSearchParams searchParams);

    EtudiantResponse findByCode(String code);
}
