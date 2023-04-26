package ma.enset.noteservice.util;

import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.model.NoteModule;
import org.mapstruct.*;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NoteModuleMapper {

    NoteModuleResponse toNoteModuleResponse(NoteModule noteModule);
    NoteModuleWithModuleResponse toNoteModuleWithModuleResponse(NoteModule noteModule);

    List<NoteModuleResponse> toNoteModuleResponseList(List<NoteModule> noteModuleList);




}
