package ma.enset.semestreservice.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Semestre {
    @Id
    private String codeSemestre;

    @NotBlank
    private String codeFiliere;

    @NotBlank
    private String intituleSemestre;
    @Min(0)

    private int nombreModules;
    @Min(0)

    private int nombreSessions;

    @ElementCollection
    List<String> modulesIds;

    @ElementCollection
    List<String> sessionsIds;
}
