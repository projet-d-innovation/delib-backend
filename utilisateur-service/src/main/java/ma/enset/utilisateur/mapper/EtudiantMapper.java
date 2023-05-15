package ma.enset.utilisateur.mapper;

import ma.enset.utilisateur.dto.utilisateur.etudiant.EtudiantCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.etudiant.EtudiantResponse;
import ma.enset.utilisateur.dto.utilisateur.etudiant.EtudiantUpdateRequest;
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

    EtudiantResponse toEtudiantResponse(Utilisateur utilisateur);

    List<EtudiantResponse> toEtudiantResponsesList(List<Utilisateur> utilisateurs);

    Utilisateur toEtudiant(EtudiantCreateRequest etudiantCreateRequest);

    List<Utilisateur> toEtudiantList(List<EtudiantCreateRequest> etudiantCreateRequests);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToEtudiant(EtudiantUpdateRequest etudiantUpdateRequest, @MappingTarget Utilisateur etudiant);

    @Mapping(target = "page", expression = "java(etudiantPage.getNumber())")
    @Mapping(target = "size", expression = "java(etudiantPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(etudiantPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(etudiantPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toEtudiantResponsesList(etudiantPage.getContent()))")
    PagingResponse<EtudiantResponse> toPagingResponse(Page<Utilisateur> etudiantPage);
}


