package ma.enset.filiereservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Filiere {
    @Id

    private String codeFiliere;

    @NotBlank
    private String intituleFiliere;
    @NotBlank
    private String codeDepartement;

    @NotBlank
    private String codeChefFiliere;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codeRegle", referencedColumnName = "codeRegle", nullable = false)
    private RegleDeCalcul regleDeCalcul;


}
