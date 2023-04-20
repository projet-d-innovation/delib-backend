package ma.enset.noteservice.util;

import ma.enset.noteservice.dto.NoteElementCreationRequest;
import ma.enset.noteservice.dto.NoteElementPagingResponse;
import ma.enset.noteservice.dto.NoteElementResponse;
import ma.enset.noteservice.dto.NoteElementUpdateRequest;
import ma.enset.noteservice.model.NoteElement;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NoteElementMapper {
    NoteElement toModule(NoteElementCreationRequest noteElementCreationRequest);

//    @Mapping(
//        target = "NoteElementId",
//        source = "noteElement.NoteElementId"
//    )
    NoteElementResponse toModuleResponse(NoteElement noteElement);
    List<NoteElement> toModuleList(List<NoteElementCreationRequest> noteElementCreationRequestList);
    List<NoteElementResponse> toModuleResponseList(List<NoteElement> noteElementList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModuleFromDTO(NoteElementUpdateRequest noteElementUpdateRequest, @MappingTarget NoteElement noteElement);

    @Mapping(target = "page", expression = "java(NoteElementPage.getNumber())")
    @Mapping(target = "size", expression = "java(NoteElementPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(NoteElementPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(NoteElementPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    NoteElementPagingResponse toPagingResponse(Page<NoteElement> NoteElementPage);

}
