package ma.enset.filiereservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Filiere implements Persistable<String> {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String codeFiliere;

    @Column(nullable = false)
    private String intituleFiliere;
    @Column(nullable = false)
    private String codeDepartement;
    private String codeChefFiliere;
    private String codeRegleDeCalcul;


    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public String getId() {
        return codeFiliere;
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
