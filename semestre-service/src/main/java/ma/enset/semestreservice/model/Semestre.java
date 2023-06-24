package ma.enset.semestreservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Semestre implements Persistable<String>  {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String codeSemestre;

    @Column(nullable = false, updatable = false)
    private String codeFiliere;

    @Column(nullable = false)
    private String intituleSemestre;

    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public String getId() {
        return codeSemestre;
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
