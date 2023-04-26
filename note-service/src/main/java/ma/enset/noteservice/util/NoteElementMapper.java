package ma.enset.noteservice.util;

import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.model.NoteElement;
import org.mapstruct.*;

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


    List<NoteElementWithElementResponse> toNoteElementWithElementResponseList(List<NoteElement> noteElementList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNoteElementFromDTO(NoteElementUpdateRequest noteElementUpdateRequest, @MappingTarget NoteElement noteElement);

    default List<String> toNoteElementIdList(List<NoteElementUpdateRequest> noteElementUpdateRequestList){
       return noteElementUpdateRequestList.stream().map(NoteElementUpdateRequest::noteElementId).toList();
    }

    default void updateNoteElementsFromDTO(List<NoteElementUpdateRequest> noteElementUpdateRequestList, List<NoteElement> noteElementList){
        for (int i = 0; i < noteElementList.size(); i++) {
            updateNoteElementFromDTO(noteElementUpdateRequestList.get(i), noteElementList.get(i));
        }
    }

    default List<String> toCodeElementList(List<ElementResponse> elementResponses){
        return elementResponses.stream().map(ElementResponse::codeElement).toList();
    }

}
