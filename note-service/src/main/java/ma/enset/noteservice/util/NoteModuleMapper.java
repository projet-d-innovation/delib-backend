package ma.enset.noteservice.util;

import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.model.NoteModule;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

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


}
