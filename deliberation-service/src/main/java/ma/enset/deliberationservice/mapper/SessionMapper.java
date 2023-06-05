package ma.enset.deliberationservice.mapper;


import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.model.Session;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SessionMapper {

    Session toSession(SessionCreationRequest sessionCreationRequest);

    List<Session> toSessionList(List<SessionCreationRequest> sessionCreationRequest);

    SessionResponse toSessionResponse(Session session);

    List<SessionResponse> toSessionResponseList(List<Session> sessionlist);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSessionFromDto(SessionUpdateRequest sessionCreationRequest, @MappingTarget Session session);

    @Mapping(target = "page", expression = "java(sessionPage.getNumber())")
    @Mapping(target = "size", expression = "java(sessionPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(sessionPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(sessionPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toSessionResponseList(sessionPage.getContent()))")
    SessionPagingResponse toPagingResponse(Page<Session> sessionPage);


}
