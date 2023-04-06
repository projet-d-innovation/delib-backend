package ma.enset.semestreservice.util;


import ma.enset.semestreservice.dto.SemestreRequestDTO;
import ma.enset.semestreservice.dto.SemestreResponseDTO;
import ma.enset.semestreservice.model.Semestre;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SemestreMapper {
    Semestre toSemestre(SemestreRequestDTO semestreRequest);

    List<Semestre> toSemestres(List<SemestreRequestDTO> semestreRequests);

    SemestreResponseDTO toSemestreResponse(Semestre semestre);

    List<SemestreResponseDTO> toSemestreResponses(List<Semestre> semestres);
}
