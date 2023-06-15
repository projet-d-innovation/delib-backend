package ma.enset.deliberationservice.model;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Session {
    @Id
    private String idSession;
    @Column(nullable = false)
    private String idInscription;
    @Column(nullable = false)
    private String codeSemestre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;

    private Double note;

    @Enumerated(EnumType.STRING)
    private SessionResult sessionResult = SessionResult.EN_COURS;

    private String previousSessionId;

    @PrePersist
    private void generateIdIfNotPresent() {
        if (idSession == null || idSession.isEmpty()) {
            idSession = UUID.randomUUID().toString();
        }
    }

}
