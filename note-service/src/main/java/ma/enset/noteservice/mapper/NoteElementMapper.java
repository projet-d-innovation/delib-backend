package ma.enset.noteservice.mapper;


import ma.enset.noteservice.dto.noteelement.NoteElementCreationRequest;
import ma.enset.noteservice.dto.noteelement.NoteElementResponse;
import ma.enset.noteservice.dto.noteelement.NoteElementUpdateRequest;
import ma.enset.noteservice.model.NoteElement;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NoteElementMapper {
    @Mapping(
            target = "note",
            expression = "java(noteCreationRequest.note().setScale(3, java.math.RoundingMode.DOWN).floatValue())"
    )
    NoteElement toNote(NoteElementCreationRequest noteCreationRequest);

    @Mapping(
            target = "note",
            expression = "java(new java.math.BigDecimal(String.valueOf(note.getNote())))"
    )
    NoteElementResponse toNoteResponse(NoteElement note);

    List<NoteElement> toNoteList(List<NoteElementCreationRequest> noteCreationRequestList);

    List<NoteElementResponse> toNoteResponseList(List<NoteElement> notelist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "codeElement", ignore = true)
    @Mapping(target = "sessionId", ignore = true)
    @Mapping(
            target = "note",
            expression = "java(noteUpdateRequest.note().setScale(3, java.math.RoundingMode.DOWN).floatValue())"
    )
    void updateNote(NoteElementUpdateRequest noteUpdateRequest, @MappingTarget NoteElement note);


    default void updateNoteList(List<NoteElementUpdateRequest> noteUpdateRequestList, @MappingTarget List<NoteElement> noteList) {
        for (int i = 0; i < noteUpdateRequestList.size(); i++) {
            updateNote(
                    noteUpdateRequestList.get(i),
                    noteList.get(i)
            );
        }
    }
}
