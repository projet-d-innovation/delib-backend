package ma.enset.utilisateur.mapper;

import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.dto.utilisateur.administrateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.administrateur.UtilisateurResponse;
import ma.enset.utilisateur.dto.utilisateur.administrateur.UtilisateurUpdateRequest;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UtilisateurMapper {

    UtilisateurResponse toUtilisateurResponse(Utilisateur utilisateur);

    List<UtilisateurResponse> toUtilisateurResponseList(List<Utilisateur> utilisateur);


    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    Utilisateur toUtilisateur(UtilisateurCreateRequest utilisateurCreateRequest);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    List<Utilisateur> toUtilisateurList(List<UtilisateurCreateRequest> utilisateurCreateRequests);

    Role mapRoleIdToRole(String roleId);

    @Named("mapRoleIdsToRoles")
    default List<Role> mapRoleIdsToRoles(List<String> roleIds) {
        if (roleIds == null) {
            return null;
        }
        return roleIds.stream()
                .map(this::mapRoleIdToRole)
                .collect(Collectors.toList());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToUtilisateur(UtilisateurUpdateRequest utilisateurUpdateRequest, @MappingTarget Utilisateur utilisateur);

    @Mapping(target = "page", expression = "java(utilisateurPage.getNumber())")
    @Mapping(target = "size", expression = "java(utilisateurPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(utilisateurPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(utilisateurPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toUtilisateurResponseList(utilisateurPage.getContent()))")
    PagingResponse<UtilisateurResponse> toPagingResponse(Page<Utilisateur> utilisateurPage);
}
