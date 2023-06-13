package ma.enset.utilisateur;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.repository.PermissionRepository;
import ma.enset.utilisateur.repository.RoleRepository;
import ma.enset.utilisateur.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {

        //.codeDepartement("GM")
        //.codeDepartement("GE")
        //.codeDepartement("MI")
        //.codeDepartement("GEG")
        //.codeDepartement("STAIC")


        List<Permission> permissions = List.of(
                Permission.builder()
                        .permissionName("READ_X")
                        .path("/api/xxxx")
                        .method("GET")
                        .groupe("GROUPE_X")
                        .build(),
                Permission.builder()
                        .permissionName("EDIT_X")
                        .path("/api/xxxx")
                        .method("PATCH")
                        .groupe("GROUPE_X")
                        .build(),
                Permission.builder()
                        .permissionName("DELETE_X")
                        .path("/api/xxxx")
                        .method("DELETE")
                        .groupe("GROUPE_X")
                        .build(),
                Permission.builder()
                        .permissionName("CREATE_X")
                        .path("/api/xxxx")
                        .method("POST")
                        .groupe("GROUPE_X")
                        .build()
        );

        log.info("Seeding permissions...");
        List<Permission> createdPermissions = permissionRepository.saveAll(permissions);
        log.info("Permissions seeded successfully.");


        List<Role> roles = List.of(
                Role.builder()
                        .roleId("CHEF_DE_DEPARTEMENT")
                        .roleName("Chef de departement")
                        .permissions(createdPermissions)
                        .groupe("ADMIN")
                        .build(),
                Role.builder()
                        .roleId("CHEF_DE_FILIERE")
                        .roleName("Chef de filiere")
                        .permissions(List.of(
                                createdPermissions.get(0),
                                createdPermissions.get(1)
                        ))
                        .groupe("ADMIN")
                        .build(),
                Role.builder()
                        .roleId("PROFESSEUR")
                        .roleName("Professeur")
                        .permissions(List.of(
                                createdPermissions.get(0)
                        ))
                        .groupe("NORMAL")
                        .build(),
                Role.builder()
                        .roleId("ETUDIANT")
                        .roleName("Etudiant")
                        .permissions(new ArrayList<>())
                        .groupe("NORMAL")
                        .build()
        );

        List<Utilisateur> utilisateurs = List.of(

                Utilisateur.builder()
                        .code("DAAIF_AZIZ")
                        .nom("DAAIF")
                        .prenom("AZIZ")
                        .codeDepartement("MI")
                        .roles(List.of(
                                Role.builder()
                                        .roleId("CHEF_DE_DEPARTEMENT")
                                        .build()
                        ))
                        .pays("MAROC")
                        .ville("MOHAMMEDIA")
                        .telephone("061231311")
                        .photo("https://media.licdn.com/dms/image/C4E03AQF9yq8nq80o7w/profile-displayphoto-shrink_800_800/0/1569832163511?e=2147483647&v=beta&t=-HpPYJf6vQyQBNlFVtoSjrXLCCelkTyQWbzsy9iXRDg")
                        .sexe("H")
                        .build(),
                Utilisateur.builder()
                        .code("KHIAT_AZZDIN")
                        .nom("KHIAT")
                        .prenom("AZZDIN")
                        .roles(List.of(
                                Role.builder()
                                        .roleId("CHEF_DE_FILIERE")
                                        .build()
                        ))
                        .pays("MAROC")
                        .ville("MOHAMMEDIA")
                        .telephone("064123412")
                        .photo("https://media.licdn.com/dms/image/D4E03AQGCXtxzw97zhg/profile-displayphoto-shrink_400_400/0/1665789873342?e=1687996800&v=beta&t=F8ZzF1g7BlDxpzTT1Pqr_-IcUqp9P0YIqSYoTv8dkCI")
                        .sexe("H")
                        .build(),
                Utilisateur.builder()
                        .code("AHRIZ_SOUAD")
                        .nom("AHRIZ")
                        .prenom("SOUAD")
                        .roles(List.of(
                                Role.builder()
                                        .roleId("CHEF_DE_FILIERE")
                                        .build()
                        ))
                        .pays("MAROC")
                        .ville("MOHAMMEDIA")
                        .telephone("065322149")
                        .sexe("H")
                        .photo("https://media.licdn.com/dms/image/C5603AQHC7jeV10j_EA/profile-displayphoto-shrink_800_800/0/1572034975660?e=2147483647&v=beta&t=7dj2ZO-ZlmenF-rXuaEQkDke4IkRcJdif8sSpALTutc")
                        .build()
        );

        log.info("Seeding roles...");
        roleRepository.saveAll(roles);
        log.info("Roles seeded successfully.");

        log.info("Seeding utilisateurs...");
        utilisateurRepository.saveAll(utilisateurs);
        log.info("Utilisateurs seeded successfully.");


    }
}
