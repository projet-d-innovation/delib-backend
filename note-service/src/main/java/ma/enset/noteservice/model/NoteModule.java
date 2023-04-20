package ma.enset.noteservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.noteservice.enums.Resultat;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NoteModule {
    @Id
    @Column(updatable = false)
    private String NoteModuleId;
    @Column(nullable = false)
    private float note;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Resultat resultat;
    @Column(nullable = false)
    private boolean redoublant;
    @Column(nullable = false)
    private boolean rattrapage;
}
