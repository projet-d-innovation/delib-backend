package ma.enset.deliberationservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.deliberationservice.model.Session;
import ma.enset.deliberationservice.model.SessionType;
import ma.enset.deliberationservice.repository.SessionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final SessionRepository repository;

    @Override
    public void run(String... args) {

        List<Session> sessions = List.of(
//   --------------------------------------------------------------------------------------------------
                Session.builder()
                        .idSession("2021-2023-ahmed.elbouchouki-1-TII11006-session1")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-ahmed.elbouchouki-1")
                        .note(11.112)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-ahmed.elbouchouki-1-TII11006-session2")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-ahmed.elbouchouki-1")
                        .note(12.514)
                        .sessionType(SessionType.SESSION2)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-ahmed.elbouchouki-1-TII12006-session1")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-ahmed.elbouchouki-1")
                        .note(11.112)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-ahmed.elbouchouki-1-TII12006-session2")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-ahmed.elbouchouki-1")
                        .note(12.514)
                        .sessionType(SessionType.SESSION2)
                        .build(),
//   --------------------------------------------------------------------------------------------------
                Session.builder()
                        .idSession("2021-2023-aymane.ouahmane-1-TII11006-session1")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-aymane.ouahmane-1")
                        .note(13.112)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-aymane.ouahmane-1-TII11006-session2")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-aymane.ouahmane-1")
                        .note(13.112)
                        .sessionType(SessionType.SESSION2)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-aymane.ouahmane-1-TII12006-session1")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-aymane.ouahmane-1")
                        .note(13.112)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-aymane.ouahmane-1-TII12006-session2")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-aymane.ouahmane-1")
                        .note(13.112)
                        .sessionType(SessionType.SESSION2)
                        .build(),
//   --------------------------------------------------------------------------------------------------
                Session.builder()
                        .idSession("2021-2023-mohammed.benchelbikh-1-TII11006-session1")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-mohammed.benchelbikh-1")
                        .note(18.431)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-mohammed.benchelbikh-1-TII11006-session2")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-mohammed.benchelbikh-1")
                        .note(18.431)
                        .sessionType(SessionType.SESSION2)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-mohammed.benchelbikh-1-TII12006-session1")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-mohammed.benchelbikh-1")
                        .note(18.431)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-mohammed.benchelbikh-1-TII12006-session2")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-mohammed.benchelbikh-1")
                        .note(18.431)
                        .sessionType(SessionType.SESSION2)
                        .build(),
//   --------------------------------------------------------------------------------------------------
                Session.builder()
                        .idSession("2021-2023-yacoub.benhamama-1-TII11006-session1")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-yacoub.benhamama-1")
                        .note(13.123)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-yacoub.benhamama-1-TII11006-session2")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-yacoub.benhamama-1")
                        .note(13.123)
                        .sessionType(SessionType.SESSION2)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-yacoub.benhamama-1-TII12006-session1")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-yacoub.benhamama-1")
                        .note(13.123)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-yacoub.benhamama-1-TII12006-session2")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-yacoub.benhamama-1")
                        .note(13.123)
                        .sessionType(SessionType.SESSION2)
                        .build(),
//   --------------------------------------------------------------------------------------------------
                Session.builder()
                        .idSession("2021-2023-zakaria.elmourtazak-1-TII11006-session1")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-zakaria.elmourtazak-1")
                        .note(9.431)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-zakaria.elmourtazak-1-TII11006-session2")
                        .codeSemestre("TII11006")
                        .idInscription("2021-2023-zakaria.elmourtazak-1")
                        .note(9.431)
                        .sessionType(SessionType.SESSION2)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-zakaria.elmourtazak-1-TII12006-session1")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-zakaria.elmourtazak-1")
                        .note(9.431)
                        .sessionType(SessionType.SESSION1)
                        .build(),
                Session.builder()
                        .idSession("2021-2023-zakaria.elmourtazak-1-TII12006-session2")
                        .codeSemestre("TII12006")
                        .idInscription("2021-2023-zakaria.elmourtazak-1")
                        .note(9.431)
                        .sessionType(SessionType.SESSION2)
                        .build()

        );

        log.debug("Seeding Session...");
        repository.saveAll(sessions);
        log.debug("Session seeded successfully.");
    }
}
