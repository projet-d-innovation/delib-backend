package ma.enset.deliberationservice.model;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
}
