package ma.enset.element.model;

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
public class Element {
    @Id
    private String codeElement;
    private String intituleElement;
    private float coefficientElement;
    private String codeModule;
    private String codeProfesseur;

}
