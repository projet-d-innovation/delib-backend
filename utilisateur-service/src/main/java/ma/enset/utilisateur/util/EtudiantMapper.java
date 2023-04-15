package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.EtudiantCreateRequestDTO;
import ma.enset.utilisateur.dto.EtudiantResponseDTO;
import ma.enset.utilisateur.dto.EtudiantUpdateRequestDTO;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface EtudiantMapper {

    EtudiantResponseDTO toEtudiantResponse(Utilisateur utilisateur);

    List<EtudiantResponseDTO> toEtudiantResponses(List<Utilisateur> utilisateurs);

    Utilisateur toUtilisateur(EtudiantCreateRequestDTO etudiantCreateRequestDTO);

    List<Utilisateur> createToUtilisateurs(List<EtudiantCreateRequestDTO> etudiantCreateRequestsDTO);


    List<String> toEtudiantCodes(List<EtudiantUpdateRequestDTO> etudiants);

    default String toEtudiantCode(EtudiantUpdateRequestDTO etudiant) {
        return etudiant.code();
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToEtudiant(EtudiantUpdateRequestDTO etudiantUpdateRequestDTO, @MappingTarget Utilisateur etudiant);


    default void updateRequestsToEtudiants(List<EtudiantUpdateRequestDTO> etudiantUpdateRequestDTOs, List<Utilisateur> etudiants) {
        for (int i = 0; i < etudiantUpdateRequestDTOs.size(); i++) {
            updateRequestToEtudiant(etudiantUpdateRequestDTOs.get(i), etudiants.get(i));
        }
    }


    @Mapping(target = "page", expression = "java(etudiantPage.getNumber())")
    @Mapping(target = "size", expression = "java(etudiantPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(etudiantPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(etudiantPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    PagingResponse<EtudiantResponseDTO> toPagingResponse(Page<Utilisateur> etudiantPage);
}


