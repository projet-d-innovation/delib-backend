package ma.enset.departementservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
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
    @Column(updatable = false)
    private String codeDepartement;

    private String codeChefDepartement;
    @NotBlank
    @Column(nullable = false)
    private String intituleDepartement;

}
