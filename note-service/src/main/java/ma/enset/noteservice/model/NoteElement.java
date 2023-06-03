package ma.enset.noteservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@IdClass(NoteElement.NoteElementId.class)
public class NoteElement {

    @Id
    @Column(nullable = false, updatable = false)
    private String codeElement;
    @Id
    @Column(nullable = false, updatable = false)
    private String sessionId;
    @Column(nullable = false)
    private float note;
    @Column(nullable = false, updatable = false)
    private boolean redoublant = false;

    public String getId() {
        return codeElement + "," + sessionId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NoteElementId implements java.io.Serializable {
        private String codeElement;
        private String sessionId;

    }
}
