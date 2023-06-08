package ma.enset.deliberationservice.dto.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ma.enset.deliberationservice.model.SessionType;

@Builder
public record SessionCreationRequest(
        @NotBlank()
        String idInscription,
        @NotBlank()
        String codeSemestre,
        @NotNull()
        SessionType sessionType,
        String previousSessionId
) {
}
