package ma.enset.noteservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@IdClass(NoteModule.NoteModuleId.class)
public class NoteModule {

    @Id
    @Column(nullable = false, updatable = false)
    private String codeModule;
    @Id
    @Column(nullable = false, updatable = false)
    private String sessionId;
    @Column(nullable = false)
    private float note;
    @Column(nullable = false, updatable = false)
    private boolean redoublant = false;

    public String getId() {
        return codeModule + "," + sessionId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NoteModuleId implements java.io.Serializable {
        private String codeModule;
        private String sessionId;

    }
}
