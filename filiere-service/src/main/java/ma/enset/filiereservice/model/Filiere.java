package ma.enset.filiereservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
