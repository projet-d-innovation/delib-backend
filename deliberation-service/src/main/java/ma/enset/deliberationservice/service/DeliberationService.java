package ma.enset.deliberationservice.service;

import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.exception.ElementNotFoundException;
import ma.enset.deliberationservice.model.SessionType;

import java.util.List;
import java.util.Set;

public interface DeliberationService {
    List<SessionResponse> get(String codeFiliere,
                              String codeSemestre,
                              String codeSessionUniversitaire,
                              int annee,
                              SessionType type);

    List<SessionResponse> deliberation(
            String codeFiliere,
            String codeSemestre,
            String codeSessionUniversitaire,
            int annee,
            SessionType type
    );

}
