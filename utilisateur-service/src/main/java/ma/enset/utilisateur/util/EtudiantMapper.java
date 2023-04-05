package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.EtudiantCreateRequestDTO;
import ma.enset.utilisateur.dto.EtudiantResponseDTO;
import ma.enset.utilisateur.dto.EtudiantUpdateRequestDTO;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface EtudiantMapper {

    @Named("toEtudiantResponse")
    EtudiantResponseDTO toEtudiantResponse(Utilisateur utilisateur);

    @Named("toEtudiantResponses")
    List<EtudiantResponseDTO> toEtudiantResponses(List<Utilisateur> utilisateurs);

    Utilisateur toUtilisateur(EtudiantCreateRequestDTO etudiantCreateRequestDTO);

    Utilisateur toUtilisateur(EtudiantUpdateRequestDTO etudiantUpdateRequestDTO);

    List<Utilisateur> createToUtilisateurs(List<EtudiantCreateRequestDTO> etudiantCreateRequestsDTO);

    List<Utilisateur> updateToUtilisateurs(List<EtudiantUpdateRequestDTO> etudiantUpdateRequestsDTO);
}
