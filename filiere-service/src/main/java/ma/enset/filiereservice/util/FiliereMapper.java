package ma.enset.filiereservice.util;


import ma.enset.filiereservice.dto.FiliereRequestDTO;
import ma.enset.filiereservice.dto.FiliereResponseDTO;
import ma.enset.filiereservice.model.Filiere;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FiliereMapper {
    Filiere toFiliere(FiliereRequestDTO filiereRequest);

    List<Filiere> toFilieres(List<FiliereRequestDTO> filiereRequests);

    FiliereResponseDTO toFiliereResponse(Filiere filiere);

    List<FiliereResponseDTO> toFiliereResponses(List<Filiere> filieres);
}
