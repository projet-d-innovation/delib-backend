package ma.enset.filiereservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Min(0)
    private int nombreEtudiants;
    @Min(0)
    private int nombreSemestres;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codeRegle", referencedColumnName = "codeRegle", nullable = false)
    private RegleDeCalcul regleDeCalcul;

    @Override
    public String toString() {
        return "Filiere{" +
                "codeFiliere='" + codeFiliere + '\'' +
                ", intituleFiliere='" + intituleFiliere + '\'' +
                ", codeDepartement='" + codeDepartement + '\'' +
                ", codeChefFiliere='" + codeChefFiliere + '\'' +
                ", nombreEtudiants=" + nombreEtudiants +
                ", nombreSemestres=" + nombreSemestres +
                ", semestreIds=" + semestreIds +
                ", anneeUniversitaireIds=" + anneeUniversitaireIds +
                '}';
    }

    @ElementCollection
    private List<String> semestreIds;
    @ElementCollection
    private List<String> anneeUniversitaireIds;






}
