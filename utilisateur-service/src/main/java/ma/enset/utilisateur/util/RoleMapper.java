package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.model.Role;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RoleMapper {
    RoleResponseDTO toRoleResponse(Role role);

    List<RoleResponseDTO> toRoleResponses(List<Role> roles);

    @Mapping(target = "permissions", source = "permissions")
    Role toRole(RoleCreateRequestDTO roleCreateRequestDTO);

    @Mapping(target = "permissions", source = "permissions")
    List<Role> createToRoles(List<RoleCreateRequestDTO> roleCreateRequestDTOS);


    Permission mapPermissionIdToPermission(Integer permissionId);

    default List<Permission> mapPermissionIdsToPermissions(List<Integer> permissionIds) {
        if (permissionIds == null) {
            return null;
        }
        return permissionIds.stream()
                .map(this::mapPermissionIdToPermission)
                .collect(Collectors.toList());
    }

    List<String> toRoleIds(List<RoleUpdateRequestDTO> roles);

    default String toRoleId(RoleUpdateRequestDTO role) {
        return role.roleId();

    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToRole(RoleUpdateRequestDTO roleUpdateRequestDTO, @MappingTarget Role role);


    default void updateRequestsToRoles(List<RoleUpdateRequestDTO> roleUpdateRequestDTOs, List<Role> roles) {
        for (int i = 0; i < roleUpdateRequestDTOs.size(); i++) {
            updateRequestToRole(roleUpdateRequestDTOs.get(i), roles.get(i));
        }
    }


    @Mapping(target = "page", expression = "java(rolePage.getNumber())")
    @Mapping(target = "size", expression = "java(rolePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(rolePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(rolePage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    PagingResponse<RoleResponseDTO> toPagingResponse(Page<Role> rolePage);

}
