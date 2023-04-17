package ma.enset.semestreservice.util;


import ma.enset.semestreservice.dto.SemestreCreationRequest;
import ma.enset.semestreservice.dto.SemestrePagingResponse;
import ma.enset.semestreservice.dto.SemestreResponse;
import ma.enset.semestreservice.dto.SemestreUpdateRequest;
import ma.enset.semestreservice.model.Semestre;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SemestreMapper {
    Semestre toSemestre(SemestreCreationRequest semestreCreationRequest);

    SemestreResponse toSemestreResponse(Semestre semestre);
    List<Semestre> toSemestreList(List<SemestreCreationRequest> semestreCreationRequestList);
    List<SemestreResponse> toSemestreResponseList(List<Semestre> semestrelist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSemestreFromDTO(SemestreUpdateRequest semestreUpdateRequest, @MappingTarget Semestre semestre);

    @Mapping(target = "page", expression = "java(semestrePage.getNumber())")
    @Mapping(target = "size", expression = "java(semestrePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(semestrePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(semestrePage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    SemestrePagingResponse toPagingResponse(Page<Semestre> semestrePage);

}
