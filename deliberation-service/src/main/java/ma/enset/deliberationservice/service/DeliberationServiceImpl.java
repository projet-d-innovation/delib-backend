package ma.enset.deliberationservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.deliberationservice.client.SemestreClient;
import ma.enset.deliberationservice.constant.CoreConstants;
import ma.enset.deliberationservice.dto.*;
import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.exception.DuplicateEntryException;
import ma.enset.deliberationservice.exception.ElementNotFoundException;
import ma.enset.deliberationservice.model.Session;
import ma.enset.deliberationservice.model.SessionType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DeliberationServiceImpl implements DeliberationService {
    private final String ELEMENT_TYPE = "Deliberation";

    private final SessionService sessionService;

    private final SemestreClient semestreClient;

    @Override
    public List<SessionResponse> get(String codeFiliere,
                                     String codeSemestre,
                                     String codeSessionUniversitaire, int annee, SessionType type) {
        List<SessionResponse> sessions = sessionService.search(codeFiliere, codeSemestre, codeSessionUniversitaire, annee, type);


        SemestreResponse semestre = semestreClient.getById(
                codeSemestre,
                true
        ).getBody();

        BigDecimal coefficientTotal = semestre.getModules().stream()
                .map(ModuleResponse::getCoefficientModule)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(3, RoundingMode.DOWN);

        for (SessionResponse sessionResponse : sessions) {
            if (sessionResponse.getNotes() == null) continue;
            BigDecimal moyenneGenerale = BigDecimal.ZERO.setScale(3, RoundingMode.DOWN);
            for (NoteModuleResponse noteModuleResponse : sessionResponse.getNotes()) {
                moyenneGenerale = moyenneGenerale.add(noteModuleResponse.getNote()
                        .setScale(3, RoundingMode.DOWN)
                        .multiply(noteModuleResponse.getModule().getCoefficientModule()));
            }
            moyenneGenerale = moyenneGenerale.divide(coefficientTotal, RoundingMode.DOWN);
            sessionResponse.setNote(moyenneGenerale);
        }

        return sessions;
    }

    @Override
    public List<SessionResponse> deliberation(String codeFiliere,
                                              String codeSemestre,
                                              String codeSessionUniversitaire, int annee, SessionType type) {
        return null;
    }
}
