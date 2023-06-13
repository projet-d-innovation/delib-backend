package ma.enset.utilisateur.mapper;

import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurUpdateRequest;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
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

    default Role mapRoleIdToRole(String roleId) {
        if (roleId == null || roleId.isBlank()) {
            return null;
        }
        return Role.builder()
                .roleId(roleId.toUpperCase())
                .build();
    }

    @Named("mapRoleIdsToRoles")
    default List<Role> mapRoleIdsToRoles(Set<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return null;
        }
        return roleIds.stream()
                .map(this::mapRoleIdToRole)
                .toList();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToUtilisateur(UtilisateurUpdateRequest utilisateurUpdateRequest, @MappingTarget Utilisateur utilisateur);

    default void updateRequestToUtilisateurList(List<UtilisateurUpdateRequest> utilisateurUpdateRequests, @MappingTarget List<Utilisateur> utilisateurList) {
        if (utilisateurUpdateRequests == null) {
            return;
        }
        for (int i = 0; i < utilisateurUpdateRequests.size(); i++) {
            UtilisateurUpdateRequest utilisateurUpdateRequest = utilisateurUpdateRequests.get(i);
            Utilisateur utilisateur = utilisateurList.get(i);
            updateRequestToUtilisateur(utilisateurUpdateRequest, utilisateur);
        }
    }

    @Mapping(target = "page", expression = "java(utilisateurPage.getNumber())")
    @Mapping(target = "size", expression = "java(utilisateurPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(utilisateurPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(utilisateurPage.getTotalElements())")
    @Mapping(target = "records", expression = "java(toUtilisateurResponseList(utilisateurPage.getContent()))")
    PagingResponse<UtilisateurResponse> toPagingResponse(Page<Utilisateur> utilisateurPage);
}
