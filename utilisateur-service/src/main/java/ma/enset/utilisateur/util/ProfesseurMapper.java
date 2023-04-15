package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProfesseurMapper {

    @Named("toProfesseurResponse")
    ProfesseurResponseDTO toProfesseurResponse(Utilisateur utilisateur);

    @Named("toProfesseurResponses")
    List<ProfesseurResponseDTO> toProfesseurResponses(List<Utilisateur> utilisateurs);

    Utilisateur toUtilisateur(ProfesseurCreateRequestDTO professeurCreateRequestDTO);

    Utilisateur toUtilisateur(ProfesseurUpdateRequestDTO professeurUpdateRequestDTO);

    List<Utilisateur> createToUtilisateurs(List<ProfesseurCreateRequestDTO> professeurCreateRequestsDTO);


    List<String> toProfesseurCodes(List<ProfesseurUpdateRequestDTO> professeurs);

    default String toProfesseurCode(ProfesseurUpdateRequestDTO professeur) {
        return professeur.code();
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToProfesseur(ProfesseurUpdateRequestDTO professeurUpdateRequestDTO, @MappingTarget Utilisateur professeur);


    default void updateRequestsToProfesseurs(List<ProfesseurUpdateRequestDTO> professeurUpdateRequestDTOs, List<Utilisateur> professeurs) {
        for (int i = 0; i < professeurUpdateRequestDTOs.size(); i++) {
            updateRequestToProfesseur(professeurUpdateRequestDTOs.get(i), professeurs.get(i));
        }
    }

    @Mapping(target = "page", expression = "java(professeurPage.getNumber())")
    @Mapping(target = "size", expression = "java(professeurPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(professeurPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(professeurPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    PagingResponse<ProfesseurResponseDTO> toPagingResponse(Page<Utilisateur> professeurPage);
}
