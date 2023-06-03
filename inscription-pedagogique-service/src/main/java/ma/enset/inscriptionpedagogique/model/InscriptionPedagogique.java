package ma.enset.inscriptionpedagogique.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class InscriptionPedagogique extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
