package ma.enset.filiereservice.config;

import ma.enset.filiereservice.util.FiliereMapper;
import ma.enset.filiereservice.util.RegleDeCalculMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public FiliereMapper filiereMapper() {
        return Mappers.getMapper(FiliereMapper.class);
    }
    @Bean
    public RegleDeCalculMapper RegleDeCalculMapper() {
        return Mappers.getMapper(RegleDeCalculMapper.class);
    }
}
