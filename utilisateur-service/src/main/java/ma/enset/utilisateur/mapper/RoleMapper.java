package ma.enset.utilisateur.mapper;

import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.dto.role.RoleCreateRequest;
import ma.enset.utilisateur.dto.role.RoleResponse;
import ma.enset.utilisateur.dto.role.RoleUpdateRequest;
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
    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roles);

    Role toRole(RoleCreateRequest roleCreateRequest);

    List<Role> toRoleList(List<RoleCreateRequest> roleCreateRequestS);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToRole(RoleUpdateRequest roleUpdateRequest, @MappingTarget Role role);

    default void updateRequestsToRoles(List<RoleUpdateRequest> roleUpdateRequests, List<Role> roles) {
        for (int i = 0; i < roleUpdateRequests.size(); i++) {
            updateRequestToRole(roleUpdateRequests.get(i), roles.get(i));
        }
    }

    default Permission fromId(Integer id) {
        return Permission.builder()
                .permissionId(id)
                .build();
    }

    List<Permission> fromIdList(List<Integer> ids);


    @Mapping(target = "page", expression = "java(rolePage.getNumber())")
    @Mapping(target = "size", expression = "java(rolePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(rolePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(rolePage.getTotalElements())")
    @Mapping(target = "records", expression = "java(toRoleResponseList(rolePage.getContent()))")
    PagingResponse<RoleResponse> toPagingResponse(Page<Role> rolePage);

}
