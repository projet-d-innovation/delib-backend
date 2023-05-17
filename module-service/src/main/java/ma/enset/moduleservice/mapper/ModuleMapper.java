package ma.enset.moduleservice.mapper;

import ma.enset.moduleservice.dto.ModuleCreationRequest;
import ma.enset.moduleservice.dto.ModuleResponse;
import ma.enset.moduleservice.dto.ModulePagingResponse;
import ma.enset.moduleservice.dto.ModuleUpdateRequest;
import ma.enset.moduleservice.model.Module;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ModuleMapper {
    Module toModule(ModuleCreationRequest moduleCreationRequest);

    List<Module> toModuleList(List<ModuleCreationRequest> request);

    @Mapping(
        target = "coefficientModule",
        expression = "java(new java.math.BigDecimal(String.valueOf(module.getCoefficientModule())))"
    )
    ModuleResponse toModuleResponse(Module module);

    List<ModuleResponse> toModuleResponseList(List<Module> moduleList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModuleFromDTO(ModuleUpdateRequest moduleUpdateRequest, @MappingTarget Module module);

    @Mapping(target = "page", expression = "java(modulePage.getNumber())")
    @Mapping(target = "size", expression = "java(modulePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(modulePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(modulePage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toModuleResponseList(modulePage.getContent()))")
    ModulePagingResponse toPagingResponse(Page<Module> modulePage);

    default void enrichModuleResponseListWithElements(List<ModuleResponse> response,
                                           List<ModuleResponse> clientResponse) {
        response.forEach(module -> {
            clientResponse.stream()
                .filter(clientModule -> Objects.equals(module.getCodeModule(), clientModule.getCodeModule()))
                .findFirst()
                .ifPresent(matchedModule -> module.setElements(matchedModule.getElements()));
        });
    }

}
