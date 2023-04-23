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
    NoteModule toModule(NoteModuleCreationRequest noteModuleCreationRequest);


    NoteModuleResponse toNoteModuleResponse(NoteModule noteModule);
    NoteModuleWithModuleResponse toNoteModuleWithModuleResponse(NoteModule noteModule);

    List<NoteModule> toNoteModuleList(List<NoteModuleCreationRequest> noteModuleCreationRequestList);
    List<NoteModuleResponse> toNoteModuleResponseList(List<NoteModule> noteModuleList);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNoteModuleFromDTO(NoteModuleUpdateRequest noteModuleUpdateRequest, @MappingTarget NoteModule noteModule);


    default void updateNoteModulesFromDTO(List<NoteModuleUpdateRequest> noteModuleUpdateRequestList, List<NoteModule> noteModuleList){
        for (int i = 0; i < noteModuleList.size(); i++) {
            updateNoteModuleFromDTO(noteModuleUpdateRequestList.get(i), noteModuleList.get(i));
        }
    }


    default List<String> toNoteModuleIdList(List<NoteModuleUpdateRequest> noteModuleUpdateRequestList){
        return noteModuleUpdateRequestList.stream().map(NoteModuleUpdateRequest::noteModuleId).toList();
    }
}
