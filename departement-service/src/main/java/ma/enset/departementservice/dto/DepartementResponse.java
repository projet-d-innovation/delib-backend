package ma.enset.departementservice.dto;

import java.util.List;

public record DepartementResponse(
        String codeDepartement,
        String intituleDepartement,
        String codeChefDepartement,
        int nombreEmployes,
        int nombreFilieres,

        List<String> filieresIds,
        List<String> usersIds
) {
}
