package ma.enset.departementservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record FiliereByDepartementResponse(
        String codeDepartement,
        List<FiliereResponse> filieres
) {
}
