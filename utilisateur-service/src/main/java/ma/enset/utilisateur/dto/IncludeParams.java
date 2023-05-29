package ma.enset.utilisateur.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class IncludeParams {
    boolean includeRoles = false;
    boolean includePermissions = false;
    boolean includeDepartement = false;
    boolean includeElements = false;
}
