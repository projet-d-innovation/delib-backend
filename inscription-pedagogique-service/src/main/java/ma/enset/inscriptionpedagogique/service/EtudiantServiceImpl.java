package ma.enset.inscriptionpedagogique.service;

import lombok.RequiredArgsConstructor;
import ma.enset.inscriptionpedagogique.client.UtilisateurClient;
import ma.enset.inscriptionpedagogique.dto.EtudiantResponse;
import ma.enset.inscriptionpedagogique.dto.RequiredSearchParams;
import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EtudiantServiceImpl implements EtudiantService {
    private final UtilisateurClient utilisateurClient;
    private final InscriptionPedagogiqueService inscriptionPedagogiqueService;

    @Override
    public List<EtudiantResponse> findAllBySearchParams(RequiredSearchParams searchParams) {

        List<EtudiantResponse> response = new ArrayList<>();

        Set<String> codesEtudiant = inscriptionPedagogiqueService.findAll(searchParams).stream()
                .map(InscriptionPedagogique::getCodeEtudiant)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!codesEtudiant.isEmpty()) {
            response.addAll(utilisateurClient.getAllEtudiantsByIds(codesEtudiant).getBody());
        }

        return response;
    }

    @Override
    public EtudiantResponse findByCode(String code) {
        EtudiantResponse response = utilisateurClient.getEtudiantById(code).getBody();
        if (response != null) {
            response.setInscriptions(
                    inscriptionPedagogiqueService.findAllByCodeEtudiant(code)
            );
        }
        return response;
    }
}
