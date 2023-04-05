package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.RoleCreateRequestDTO;
import ma.enset.utilisateur.dto.RoleResponseDTO;
import ma.enset.utilisateur.dto.RoleUpdateRequestDTO;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RoleMapper {
    @Named("toRoleWithPermsResponse")
    RoleResponseDTO toRoleWithPermsResponse(Role role);

    @Named("toRoleWithPermsResponses")
    List<RoleResponseDTO> toRoleWithPermsResponses(List<Role> roles);

    @Named("toRoleWithoutPermsResponse")
    @Mapping(target = "permissions", ignore = true)
    RoleResponseDTO toRoleWithoutPermsResponse(Role role);

    @Named("toRoleWithoutPermsResponses")
    @Mapping(target = "permissions", ignore = true)
    List<RoleResponseDTO> toRoleWithoutPermsResponses(List<Role> roles);

    @Named("toRoleCreateRequestDTO")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissionIdsToPermissions")
    Role toRole(RoleCreateRequestDTO roleCreateRequestDTO);

    @Named("toRoleUpdateRequestDTO")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissionIdsToPermissions")
    Role toRole(RoleUpdateRequestDTO roleUpdateRequestDTO);

    @Named("toRoleCreateRequestDTOs")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissionIdsToPermissions")
    List<Role> createToRoles(List<RoleCreateRequestDTO> roleCreateRequestDTOS);

    @Named("toRoleUpdateRequestDTOs")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissionIdsToPermissions")
    List<Role> updateToRoles(List<RoleUpdateRequestDTO> roleUpdateRequestDTOS);


    Permission mapPermissionIdToPermission(Integer permissionId);

    @Named("mapPermissionIdsToPermissions")
    default List<Permission> mapPermissionIdsToPermissions(List<Integer> permissionIds) {
        if (permissionIds == null) {
            return null;
        }
        return permissionIds.stream()
                .map(this::mapPermissionIdToPermission)
                .collect(Collectors.toList());
    }

}
