package ma.enset.filiereservice.util;


import ma.enset.filiereservice.dto.FiliereRequestDTO;
import ma.enset.filiereservice.dto.FiliereResponseDTO;
import ma.enset.filiereservice.model.Filiere;

import java.util.List;


public interface FiliereMapper {
    Filiere toFiliere(FiliereRequestDTO filiereRequest);

    List<Filiere> toFilieres(List<FiliereRequestDTO> filiereRequests);

    FiliereResponseDTO toFiliereResponse(Filiere filiere);

    List<FiliereResponseDTO> toFiliereResponses(List<Filiere> filieres);
}
