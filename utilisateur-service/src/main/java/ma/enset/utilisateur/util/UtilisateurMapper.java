package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.*;
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

    @Named("toUtilisateurWithoutRoleResponse")
    @Mapping(target = "roles", ignore = true)
    UtilisateurResponseDTO toUtilisateurWithoutRoleResponse(Utilisateur utilisateur);

    @Named("toUtilisateurWithoutRoleResponses")
    UtilisateurResponseDTO toUtilisateurWithRoleResponse(Utilisateur utilisateur);

    @Named("toUtilisateurWithRoleResponse")
    @Mapping(target = "roles", ignore = true)
    List<UtilisateurResponseDTO> toUtilisateurWithoutRoleResponses(List<Utilisateur> utilisateurs);

    @Named("toUtilisateurWithRoleResponses")
    List<UtilisateurResponseDTO> toUtilisateurWithRoleResponses(List<Utilisateur> utilisateurs);

    @Named("toUtilisateurWithRoleAndPermsResponse")
    UtilisateurNestedRolesResponseDTO toUtilisateurWithRoleAndPermsResponse(Utilisateur utilisateur);

    @Named("toUtilisateurWithRoleAndPermsResponses")
    List<UtilisateurNestedRolesResponseDTO> toUtilisateurWithRoleAndPermsResponses(List<Utilisateur> utilisateurs);


    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    Utilisateur toUtilisateur(UtilisateurCreateRequestDTO utilisateurCreateRequestDTO);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    List<Utilisateur> createToUtilisateurs(List<UtilisateurCreateRequestDTO> utilisateurCreateRequestsDTO);


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


    List<String> toUtilisateurCodes(List<UtilisateurUpdateRequestDTO> utilisateurs);

    default String toUtilisateurCode(UtilisateurUpdateRequestDTO utilisateur) {
        return utilisateur.code();
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToUtilisateur(UtilisateurUpdateRequestDTO utilisateurUpdateRequestDTO, @MappingTarget Utilisateur utilisateur);


    default void updateRequestsToUtilisateurs(List<UtilisateurUpdateRequestDTO> utilisateurUpdateRequestDTOs, List<Utilisateur> utilisateurs) {
        for (int i = 0; i < utilisateurUpdateRequestDTOs.size(); i++) {
            updateRequestToUtilisateur(utilisateurUpdateRequestDTOs.get(i), utilisateurs.get(i));
        }
    }


    @Mapping(target = "page", expression = "java(utilisateurPage.getNumber())")
    @Mapping(target = "size", expression = "java(utilisateurPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(utilisateurPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(utilisateurPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    PagingResponse<UtilisateurResponseDTO> toPagingResponse(Page<UtilisateurResponseDTO> utilisateurPage);
}
