package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.ProfesseurCreateRequestDTO;
import ma.enset.utilisateur.dto.ProfesseurResponseDTO;
import ma.enset.utilisateur.dto.ProfesseurUpdateRequestDTO;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ProfesseurMapper {

    @Named("toProfesseurResponse")
    ProfesseurResponseDTO toProfesseurResponse(Utilisateur utilisateur);

    @Named("toProfesseurResponses")
    List<ProfesseurResponseDTO> toProfesseurResponses(List<Utilisateur> utilisateurs);

    Utilisateur toUtilisateur(ProfesseurCreateRequestDTO professeurCreateRequestDTO);

    Utilisateur toUtilisateur(ProfesseurUpdateRequestDTO professeurUpdateRequestDTO);

    List<Utilisateur> createToUtilisateurs(List<ProfesseurCreateRequestDTO> professeurCreateRequestsDTO);

    List<Utilisateur> updateToUtilisateurs(List<ProfesseurUpdateRequestDTO> professeurUpdateRequestsDTO);
}
