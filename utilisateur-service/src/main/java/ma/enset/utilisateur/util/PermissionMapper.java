package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.PermissionCreateRequestDTO;
import ma.enset.utilisateur.dto.PermissionResponseDTO;
import ma.enset.utilisateur.dto.PermissionUpdateRequestDTO;
import ma.enset.utilisateur.model.Permission;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PermissionMapper {
    PermissionResponseDTO toPermissionResponse(Permission permission);

    List<PermissionResponseDTO> toPermissionResponses(List<Permission> permissions);


    Permission toPermission(PermissionCreateRequestDTO permissionCreateRequestDTO);

    List<Permission> createToPermissions(List<PermissionCreateRequestDTO> permissionRequestsDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToPermission(PermissionUpdateRequestDTO permissionUpdateRequestDTO, @MappingTarget Permission permission);


    default void updateRequestsToPermissions(List<PermissionUpdateRequestDTO> permissionUpdateRequestDTOs, List<Permission> permissions) {
        for (int i = 0; i < permissionUpdateRequestDTOs.size(); i++) {
            updateRequestToPermission(permissionUpdateRequestDTOs.get(i), permissions.get(i));
        }
    }

    List<Integer> toPermissionIds(List<PermissionUpdateRequestDTO> permissions);

    default Integer toPermissionId(PermissionUpdateRequestDTO permission) {
        return permission.permissionId();

    }

    @Mapping(target = "page", expression = "java(permissionPage.getNumber())")
    @Mapping(target = "size", expression = "java(permissionPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(permissionPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(permissionPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    PagingResponse<PermissionResponseDTO> toPagingResponse(Page<Permission> permissionPage);

}
