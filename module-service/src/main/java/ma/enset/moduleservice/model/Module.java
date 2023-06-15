package ma.enset.moduleservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Module implements Persistable<String> {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String codeModule;

    @Column(nullable = false)
    private String intituleModule;

    @Column(nullable = false)
    private float coefficientModule;

    @Column(nullable = false, updatable = false)
    private String codeSemestre;

    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public String getId() {
        return codeModule;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}
