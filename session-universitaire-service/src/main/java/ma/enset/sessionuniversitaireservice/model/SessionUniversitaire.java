package ma.enset.sessionuniversitaireservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionUniversitaire {
    @Id
    @Column(updatable = false)
    private String id;
    @Column(nullable = false, updatable = false)
    private LocalDate startDate;
    @Column(nullable = false, updatable = false)
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatSessionUniversitaire etat;

    @PrePersist
    private void setDefaultEtatValue() {
        if (this.etat == null) {
            this.etat = EtatSessionUniversitaire.EN_COURS;
        }
    }
}
