package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.UtilisateurCreateRequestDTO;
import ma.enset.utilisateur.dto.UtilisateurNestedRolesResponseDTO;
import ma.enset.utilisateur.dto.UtilisateurResponseDTO;
import ma.enset.utilisateur.dto.UtilisateurUpdateRequestDTO;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
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


    @Named("toUtilisateurCreateRequestDTO")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    Utilisateur toUtilisateur(UtilisateurCreateRequestDTO utilisateurCreateRequestDTO);

    @Named("toUtilisateurCreateRequestDTOs")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    List<Utilisateur> createToUtilisateurs(List<UtilisateurCreateRequestDTO> utilisateurCreateRequestsDTO);

    @Named("toUtilisateurUpdateRequestDTO")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    Utilisateur toUtilisateur(UtilisateurUpdateRequestDTO utilisateurCreateRequestDTO);

    @Named("toUtilisateurUpdateRequestDTOs")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    List<Utilisateur> updateToUtilisateurs(List<UtilisateurUpdateRequestDTO> utilisateurCreateRequestsDTO);


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
}
