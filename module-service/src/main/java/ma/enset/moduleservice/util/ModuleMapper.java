package ma.enset.moduleservice.util;

import ma.enset.moduleservice.dto.ModuleCreationRequest;
import ma.enset.moduleservice.dto.ModuleResponse;
import ma.enset.moduleservice.dto.ModulePagingResponse;
import ma.enset.moduleservice.dto.ModuleUpdateRequest;
import ma.enset.moduleservice.model.Module;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ModuleMapper {
    Module toModule(ModuleCreationRequest moduleCreationRequest);

    @Mapping(
        target = "coefficientModule",
        expression = "java(new java.math.BigDecimal( String.valueOf(module.getCoefficientModule())) )"
    )
    ModuleResponse toModuleResponse(Module module);
    List<Module> toModuleList(List<ModuleCreationRequest> moduleCreationRequestList);
    List<ModuleResponse> toModuleResponseList(List<Module> moduleList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModuleFromDTO(ModuleUpdateRequest moduleUpdateRequest, @MappingTarget Module module);

    @Mapping(target = "page", expression = "java(modulePage.getNumber())")
    @Mapping(target = "size", expression = "java(modulePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(modulePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(modulePage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    ModulePagingResponse toPagingResponse(Page<Module> modulePage);

}
