package ma.enset.moduleservice.model;

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
public class Module {
    @Id
    @Column(updatable = false)
    private String codeModule;
    @Column(nullable = false)
    private String intituleModule;
    @Column(nullable = false)
    private float coefficientModule;
    @Column(nullable = false, updatable = false)
    private String codeSemestre;
}
