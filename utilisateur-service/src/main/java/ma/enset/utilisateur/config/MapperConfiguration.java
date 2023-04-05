package ma.enset.utilisateur.config;

import ma.enset.utilisateur.util.*;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public EtudiantMapper etudiantMapper() {
        return Mappers.getMapper(EtudiantMapper.class);
    }

    @Bean
    public UtilisateurMapper utilisateurMapper() {
        return Mappers.getMapper(UtilisateurMapper.class);
    }

    @Bean
    public ProfesseurMapper professeurMapper() {
        return Mappers.getMapper(ProfesseurMapper.class);
    }

    @Bean
    public RoleMapper roleMapper() {
        return Mappers.getMapper(RoleMapper.class);
    }

    @Bean
    public PermissionMapper permissionMapper() {
        return Mappers.getMapper(PermissionMapper.class);
    }
}
