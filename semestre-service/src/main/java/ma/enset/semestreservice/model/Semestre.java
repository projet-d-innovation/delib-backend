package ma.enset.semestreservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Semestre {
    @Id
    private String codeSemestre;

    private String codeFiliere;

    private String intituleSemestre;
}
