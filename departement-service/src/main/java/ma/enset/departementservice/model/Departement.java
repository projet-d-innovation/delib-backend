package ma.enset.departementservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Departement {
    @Id

    private String codeDepartement;

    @NotBlank
    private String codeChefDepartement;
    @NotBlank
    private String intituleDepartement;
}
