package ma.enset.element.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Element implements Persistable<String> {
    @Id
    @Column(nullable = false, updatable = false)
    private String codeElement;

    @Column(nullable = false)
    private String intituleElement;

    @Column(nullable = false)
    private float coefficientElement;

    @Column(nullable = false, updatable = false)
    private String codeModule;

    private String codeProfesseur;

    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public String getId() {
        return codeElement;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}
