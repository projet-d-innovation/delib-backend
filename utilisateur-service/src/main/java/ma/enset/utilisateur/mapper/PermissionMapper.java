package ma.enset.utilisateur.mapper;

import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.permission.PermissionCreateRequest;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.permission.PermissionUpdateRequest;
import ma.enset.utilisateur.model.Permission;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PermissionMapper {
    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toPermissionResponseList(List<Permission> permissions);

    Permission toPermission(PermissionCreateRequest permissionCreateRequest);

    List<Permission> toPermissionList(List<PermissionCreateRequest> permissionRequests);


    List<Integer> toPermissionIdList(List<PermissionUpdateRequest> permissionList);

    default Integer toPermissionId(PermissionUpdateRequest permission) {
        return permission.permissionId();
    }


//    default Permission fromId(Integer id) {
//        return Permission.builder().permissionId(id).build();
//    }
//
//    List<Permission> fromIdList(Set<Integer> id);

    Permission fromResponse(PermissionResponse permissionResponse);

    List<Permission> fromResponseList(List<PermissionResponse> permissionResponseList);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToPermission(PermissionUpdateRequest permissionUpdateRequest, @MappingTarget Permission permission);


    default void updateRequestsToPermissions(List<PermissionUpdateRequest> permissionUpdateRequests, List<Permission> permissions) {
        for (int i = 0; i < permissionUpdateRequests.size(); i++) {
            updateRequestToPermission(permissionUpdateRequests.get(i), permissions.get(i));
        }
    }

    @Mapping(target = "page", expression = "java(permissionPage.getNumber())")
    @Mapping(target = "size", expression = "java(permissionPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(permissionPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(permissionPage.getTotalElements())")
    @Mapping(target = "records", expression = "java(toPermissionResponseList(permissionPage.getContent()))")
    PagingResponse<PermissionResponse> toPagingResponse(Page<Permission> permissionPage);

}
