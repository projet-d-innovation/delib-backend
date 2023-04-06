package ma.enset.filiereservice.util;


import ma.enset.filiereservice.dto.RegleDeCalculRequestDTO;
import ma.enset.filiereservice.dto.RegleDeCalculResponseDTO;
import ma.enset.filiereservice.model.RegleDeCalcul;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RegleDeCalculMapper {
    RegleDeCalcul toRegleDeCalcul(RegleDeCalculRequestDTO RegleDeCalculRequest);

    List<RegleDeCalcul> toRegleDeCalculs(List<RegleDeCalculRequestDTO> RegleDeCalculRequests);

    RegleDeCalculResponseDTO toRegleDeCalculResponse(RegleDeCalcul RegleDeCalcul);

    List<RegleDeCalculResponseDTO> toRegleDeCalculResponses(List<RegleDeCalcul> RegleDeCalculs);
}
