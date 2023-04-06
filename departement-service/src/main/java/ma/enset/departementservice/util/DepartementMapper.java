package ma.enset.departementservice.util;


import ma.enset.departementservice.dto.DepartementRequestDTO;
import ma.enset.departementservice.dto.DepartementResponseDTO;
import ma.enset.departementservice.model.Departement;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DepartementMapper {
    Departement toDepartement(DepartementRequestDTO departementRequest);

    List<Departement> toDepartements(List<DepartementRequestDTO> departementRequests);

    DepartementResponseDTO toDepartementResponse(Departement departement);

    List<DepartementResponseDTO> toDepartementResponses(List<Departement> departements);
}
