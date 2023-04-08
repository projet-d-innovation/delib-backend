package ma.enset.filiereservice.util;

import ma.enset.filiereservice.dto.FiliereRequestDTO;
import ma.enset.filiereservice.dto.FiliereResponseDTO;
import ma.enset.filiereservice.model.Filiere;

import java.util.ArrayList;
import java.util.List;

public class FiliereMapperImpl implements FiliereMapper{
    @Override
    public Filiere toFiliere(FiliereRequestDTO filiereRequest) {
        if(filiereRequest == null) {
            return null;
        }
        return Filiere.builder().intituleFiliere(filiereRequest.intituleFiliere())
                .codeFiliere(filiereRequest.codeFiliere())
                .codeDepartement(filiereRequest.codeDepartement())
                .codeRegleDeCalcul(filiereRequest.codeRegleDeCalcul())
                .semestres(null)
                .regleDeCalcul(null)
                .build();
    }

    @Override
    public List<Filiere> toFilieres(List<FiliereRequestDTO> filiereRequests) {
        if (filiereRequests == null) {
            return null;
        }
        List<Filiere> list = new ArrayList<>();
        filiereRequests.forEach(filiereRequest -> list.add(toFiliere(filiereRequest)));
        return list;
    }

    @Override
    public FiliereResponseDTO toFiliereResponse(Filiere filiere) {
        if (filiere == null) {
            return null;
        }
        return new FiliereResponseDTO(
                filiere.getCodeFiliere(),
                filiere.getIntituleFiliere(),
                filiere.getCodeRegleDeCalcul(),
                filiere.getCodeDepartement()
        );
    }

    @Override
    public List<FiliereResponseDTO> toFiliereResponses(List<Filiere> filieres) {
        if (filieres == null) {
            return null;
        }
        List<FiliereResponseDTO> list = new ArrayList<>();
        filieres.forEach(filiere -> list.add(toFiliereResponse(filiere)));
        return list;
    }
}
