package ma.enset.departementservice.config;

import ma.enset.departementservice.util.DepartementMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public DepartementMapper departementMapper() {
        return Mappers.getMapper(DepartementMapper.class);
    }

}
