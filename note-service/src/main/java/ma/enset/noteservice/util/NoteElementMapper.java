package ma.enset.noteservice.util;

import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.model.NoteElement;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NoteElementMapper {
    NoteElement toNoteElement(NoteElementCreationRequest noteElementCreationRequest);


    NoteElementResponse toNoteElementResponse(NoteElement noteElement);

    NoteElementWithElementResponse toNoteElementWithElementResponse(NoteElement noteElement);
    List<NoteElement> toNoteElementList(List<NoteElementCreationRequest> noteElementCreationRequestList);
    List<NoteElementResponse> toNoteElementResponseList(List<NoteElement> noteElementList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNoteElementFromDTO(NoteElementUpdateRequest noteElementUpdateRequest, @MappingTarget NoteElement noteElement);






    List<NoteElementWithElementResponse> toNoteElementWithElementResponseList(List<NoteElement> noteElementList);
}
