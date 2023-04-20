package ma.enset.noteservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NoteElement {
    @Id
    @Column(updatable = false)
    private String NoteElementId;
    @Column(nullable = false)
    private float note;
    @Column(nullable = false)
    private boolean redoublant;

}
