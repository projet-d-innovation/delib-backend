package ma.enset.noteservice.mapper;


import ma.enset.noteservice.dto.notemodule.NoteModuleCreationRequest;
import ma.enset.noteservice.dto.notemodule.NoteModuleResponse;
import ma.enset.noteservice.model.NoteModule;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NoteModuleMapper {
    @Mapping(
            target = "note",
            expression = "java(noteCreationRequest.note().setScale(3, java.math.RoundingMode.DOWN).floatValue())"
    )
    NoteModule toNote(NoteModuleCreationRequest noteCreationRequest);

    @Mapping(
            target = "note",
            expression = "java(new java.math.BigDecimal(String.valueOf(note.getNote())))"
    )
    NoteModuleResponse toNoteResponse(NoteModule note);

    List<NoteModule> toNoteList(List<NoteModuleCreationRequest> noteCreationRequestList);

    List<NoteModuleResponse> toNoteResponseList(List<NoteModule> notelist);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "codeModule", ignore = true)
//    @Mapping(target = "sessionId", ignore = true)
//    @Mapping(
//            target = "note",
//            expression = "java(noteUpdateRequest.note().setScale(3, java.math.RoundingMode.DOWN).floatValue())"
//    )
//    void updateNote(NoteModuleUpdateRequest noteUpdateRequest, @MappingTarget NoteModule note);
//
//
//    default void updateNoteList(List<NoteModuleUpdateRequest> noteUpdateRequestList, @MappingTarget List<NoteModule> noteList) {
//        for (int i = 0; i < noteUpdateRequestList.size(); i++) {
//            updateNote(
//                    noteUpdateRequestList.get(i),
//                    noteList.get(i)
//            );
//        }
//    }
}
