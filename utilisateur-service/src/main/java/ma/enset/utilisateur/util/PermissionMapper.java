package ma.enset.utilisateur.util;

import ma.enset.utilisateur.dto.PermissionCreateRequestDTO;
import ma.enset.utilisateur.dto.PermissionResponseDTO;
import ma.enset.utilisateur.dto.PermissionUpdateRequestDTO;
import ma.enset.utilisateur.model.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    PermissionResponseDTO toPermissionResponse(Permission permission);

    Permission toPermission(PermissionCreateRequestDTO permissionCreateRequestDTO);

    Permission toPermission(PermissionUpdateRequestDTO permissionUpdateRequestDTO);

    List<PermissionResponseDTO> toPermissionResponses(List<Permission> permissions);

    List<Permission> createToPermissions(List<PermissionCreateRequestDTO> permissionRequestsDTO);

    List<Permission> updateToPermissions(List<PermissionUpdateRequestDTO> permissionRequestsDTO);


}
