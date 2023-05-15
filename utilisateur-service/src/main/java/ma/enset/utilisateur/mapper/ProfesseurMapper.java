package ma.enset.utilisateur.mapper;

import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.dto.utilisateur.professeur.ProfesseurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.professeur.ProfesseurResponse;
import ma.enset.utilisateur.dto.utilisateur.professeur.ProfesseurUpdateRequest;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProfesseurMapper {

    ProfesseurResponse toProfesseurResponse(Utilisateur utilisateur);

    List<ProfesseurResponse> toProfesseurResponseList(List<Utilisateur> utilisateurs);

    Utilisateur toProfesseur(ProfesseurCreateRequest professeurCreateRequest);

    List<Utilisateur> toProfesseurList(List<ProfesseurCreateRequest> professeurCreateRequests);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToProfesseur(ProfesseurUpdateRequest professeurUpdateRequest, @MappingTarget Utilisateur professeur);

    @Mapping(target = "page", expression = "java(professeurPage.getNumber())")
    @Mapping(target = "size", expression = "java(professeurPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(professeurPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(professeurPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toProfesseurResponseList(professeurPage.getContent()))")
    PagingResponse<ProfesseurResponse> toPagingResponse(Page<Utilisateur> professeurPage);
}
