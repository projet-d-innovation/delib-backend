package ma.enset.inscriptionpedagogique.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InscriptionPedagogique extends AbstractEntity<String> {

    @Id
    @Column(updatable = false)
    private String id;

    @Column(nullable = false, updatable = false)
    private String codeEtudiant;

    @Column(nullable = false, updatable = false)
    private String codeFiliere;

    @Column(nullable = false, updatable = false)
    private String codeSessionUniversitaire;

    @Column(nullable = false, updatable = false)
    private int annee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatInscriptionPedagogique etat;

    @Column(updatable = false)
    private float note;

    @Override
    protected void prePersist() {
        super.prePersist();

        if (this.etat == null) {
            this.etat = EtatInscriptionPedagogique.EN_COURS;
        }
    }
}
