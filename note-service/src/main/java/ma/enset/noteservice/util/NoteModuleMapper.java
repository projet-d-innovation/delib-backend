package ma.enset.noteservice.util;

import ma.enset.noteservice.dto.NoteModuleCreationRequest;
import ma.enset.noteservice.dto.NoteModulePagingResponse;
import ma.enset.noteservice.dto.NoteModuleResponse;
import ma.enset.noteservice.dto.NoteModuleUpdateRequest;
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


    NoteModuleResponse toModuleResponse(NoteModule noteModule);
    List<NoteModule> toModuleList(List<NoteModuleCreationRequest> noteModuleCreationRequestList);
    List<NoteModuleResponse> toModuleResponseList(List<NoteModule> noteModuleList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModuleFromDTO(NoteModuleUpdateRequest noteModuleUpdateRequest, @MappingTarget NoteModule noteModule);

    @Mapping(target = "page", expression = "java(NoteModulePage.getNumber())")
    @Mapping(target = "size", expression = "java(NoteModulePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(NoteModulePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(NoteModulePage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    NoteModulePagingResponse toPagingResponse(Page<NoteModule> NoteModulePage);

}
