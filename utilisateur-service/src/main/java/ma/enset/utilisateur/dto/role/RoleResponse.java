package ma.enset.utilisateur.dto.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.enset.utilisateur.dto.permission.PermissionResponse;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class RoleResponse {
    private String roleId;
    private String roleName;
    private String groupe;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PermissionResponse> permissions;

}

