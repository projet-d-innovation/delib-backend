package ma.enset.sessionuniversitaireservice.mapper;


import ma.enset.sessionuniversitaireservice.dto.SessionCreationRequest;
import ma.enset.sessionuniversitaireservice.dto.SessionPagingResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionUpdateRequest;
import ma.enset.sessionuniversitaireservice.model.SessionUniversitaire;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SessionUniversitaireMapper {
    @Mapping(target = "id", expression = "java(request.id())")
    SessionUniversitaire toSessionUniversitaire(SessionCreationRequest request);
    SessionResponse toSessionResponse(SessionUniversitaire sessionUniversitaire);
    List<SessionUniversitaire> toSessionUniversitaireList(List<SessionCreationRequest> request);
    List<SessionResponse> toSessionResponseList(List<SessionUniversitaire> sessionUniversitaireList);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSessionUniversitaireFromDTO(SessionUpdateRequest request, @MappingTarget SessionUniversitaire sessionUniversitaire);
    @Mapping(target = "page", expression = "java(sessionUniversitairePage.getNumber())")
    @Mapping(target = "size", expression = "java(sessionUniversitairePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(sessionUniversitairePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(sessionUniversitairePage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toSessionResponseList(sessionUniversitairePage.getContent()))")
    SessionPagingResponse toPagingResponse(Page<SessionUniversitaire> sessionUniversitairePage);
}
