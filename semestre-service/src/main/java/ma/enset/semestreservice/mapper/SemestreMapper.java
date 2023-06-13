package ma.enset.semestreservice.mapper;

import ma.enset.semestreservice.dto.SemestreCreationRequest;
import ma.enset.semestreservice.dto.SemestrePagingResponse;
import ma.enset.semestreservice.dto.SemestreResponse;
import ma.enset.semestreservice.dto.SemestreUpdateRequest;
import ma.enset.semestreservice.model.Semestre;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SemestreMapper {
    Semestre toSemestre(SemestreCreationRequest request);

    SemestreResponse toSemestreResponse(Semestre semestre);

    List<Semestre> toSemestreList(List<SemestreCreationRequest> request);

    List<SemestreResponse> toSemestreResponseList(List<Semestre> semestreList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSemestreFromDTO(SemestreUpdateRequest semestreUpdateRequest, @MappingTarget Semestre semestre);

    @Mapping(target = "page", expression = "java(semestrePage.getNumber())")
    @Mapping(target = "size", expression = "java(semestrePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(semestrePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(semestrePage.getTotalElements())")
    @Mapping(target = "records", expression = "java(toSemestreResponseList(semestrePage.getContent()))")
    SemestrePagingResponse toPagingResponse(Page<Semestre> semestrePage);

    default void enrichSemestreResponseListWithModuless(List<SemestreResponse> response, List<SemestreResponse> clientResponse) {
        response.forEach(semestre -> {
            clientResponse.stream()
                .filter(clientSemestre -> Objects.equals(semestre.getCodeSemestre(), clientSemestre.getCodeSemestre()))
                .findFirst()
                .ifPresent(matchedSemestre -> semestre.setModules(matchedSemestre.getModules()));
        });
    }

}
