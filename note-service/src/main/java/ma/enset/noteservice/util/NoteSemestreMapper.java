package ma.enset.noteservice.util;

import ma.enset.noteservice.dto.NoteSemestreResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NoteSemestreMapper {

    static NoteSemestreResponse toNoteSemestreResponse(String codeSemestre, float v) {
        return new NoteSemestreResponse(codeSemestre, v);
    }

}
