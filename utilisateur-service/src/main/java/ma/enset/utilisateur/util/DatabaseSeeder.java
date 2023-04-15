package ma.enset.utilisateur.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.repository.PermissionRepository;
import ma.enset.utilisateur.repository.RoleRepository;
import ma.enset.utilisateur.repository.UtilisateurRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final UtilisateurRepository utilisateurRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;
    private final Faker faker = new Faker();

    @Override
    public void run(String... args) {

        List<Permission> permissionList = Arrays.asList(
                Permission.builder()
                        .permissionId(1)
                        .permissionName("CAN READ USERS")
                        .path("/api/v1/utilisateurs")
                        .method("GET")
                        .build(),
                Permission.builder()
                        .permissionId(2)
                        .permissionName("CAN CREATE USERS")
                        .path("/api/v1/utilisateurs")
                        .method("POST")
                        .build(),
                Permission.builder()
                        .permissionId(3)
                        .permissionName("CAN DELETE USERS")
                        .path("/api/v1/utilisateurs")
                        .method("DELETE")
                        .build()
        );


        List<Role> roleList = Arrays.asList(
                Role.builder()
                        .roleId("ADMIN")
                        .roleName("administrateur")
                        .permissions(List.of(permissionList.get(0), permissionList.get(1), permissionList.get(2)))
                        .build(),
                Role.builder()
                        .roleId("PROFESSEUR")
                        .roleName("professeur")
                        .permissions(List.of(permissionList.get(0), permissionList.get(1)))
                        .build(),
                Role.builder()
                        .roleId("ETUDIANT")
                        .roleName("etudiant")
                        .permissions(List.of(permissionList.get(0)))
                        .build()
        );

        List<Utilisateur> utilisateurList = IntStream.rangeClosed(1, 100)
                .mapToObj(i ->
                        Utilisateur.builder()
                                .code(faker.aws().accountId())
                                .cin("CIN" + faker.number().digits(8))
                                .cne("CNE" + faker.number().digits(8))
                                .nom(faker.name().lastName())
                                .prenom(faker.name().firstName())
                                .telephone(faker.phoneNumber().cellPhone())
                                .adresse(faker.address().fullAddress())
                                .photo(faker.internet().url())
                                .dateNaissance(faker.date().birthday().toLocalDateTime())
                                .ville(faker.address().city())
                                .pays(faker.address().country())
                                .roles(List.of(roleList.get(faker.number().numberBetween(0, roleList.size()))))
                                .build()
                ).toList();

        log.info("Database seeding: started");
        permissionRepository.saveAll(permissionList);
        roleRepository.saveAll(roleList);
        utilisateurRepository.saveAll(utilisateurList);

        log.info("Database seeding: completed with success");

    }
}
