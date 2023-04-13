package ma.enset.element.model;

import jakarta.persistence.Column;
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
    @Column(updatable = false)
    private String codeElement;
    @Column(nullable = false)
    private String intituleElement;
    @Column(nullable = false)
    private float coefficientElement;
    @Column(nullable = false, updatable = false)
    private String codeModule;

    private String codeProfesseur;

}
