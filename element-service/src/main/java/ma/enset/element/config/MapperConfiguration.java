package ma.enset.element.config;

import ma.enset.element.util.ElementMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public ElementMapper elementMapper() {
        return Mappers.getMapper(ElementMapper.class);
    }
}
