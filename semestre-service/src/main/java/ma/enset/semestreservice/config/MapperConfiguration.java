package ma.enset.semestreservice.config;

import ma.enset.semestreservice.util.SemestreMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public SemestreMapper semestreMapper() {
        return Mappers.getMapper(SemestreMapper.class);
    }

}
