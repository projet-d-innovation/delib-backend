package ma.enset.filiereservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codeRegle", referencedColumnName = "codeRegle", nullable = false)
    private RegleDeCalcul regleDeCalcul;

    @Transient
    private String codeRegleDeCalcul;


    @ElementCollection
    @CollectionTable(name = "semestres", joinColumns = @JoinColumn(name = "codeFiliere"))
    @Column(name = "semestre")
    private Set<String> semestres;

}
