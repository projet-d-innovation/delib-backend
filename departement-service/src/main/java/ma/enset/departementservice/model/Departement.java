package ma.enset.departementservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Departement implements Persistable<String> {
    @Id
    @Column(updatable = false)
    private String codeDepartement;

    private String codeChefDepartement;
    @NotBlank
    @Column(nullable = false)
    private String intituleDepartement;


    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public String getId() {
        return codeDepartement;
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
