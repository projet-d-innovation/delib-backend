package ma.enset.filiereservice.mapper;


import ma.enset.filiereservice.dto.FiliereCreationRequest;
import ma.enset.filiereservice.dto.FilierePagingResponse;
import ma.enset.filiereservice.dto.FiliereResponse;
import ma.enset.filiereservice.dto.FiliereUpdateRequest;
import ma.enset.filiereservice.model.Filiere;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface FiliereMapper {

    Filiere toFiliere(FiliereCreationRequest filiereCreationRequest);

    List<Filiere> toFiliereList(List<FiliereCreationRequest> filiereCreationRequest);

    FiliereResponse toFiliereResponse(Filiere filiere);

    List<FiliereResponse> toFiliereResponseList(List<Filiere> filierelist);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFiliereFromDto(FiliereUpdateRequest filiereCreationRequest, @MappingTarget Filiere filiere);

    @Mapping(target = "page", expression = "java(filierePage.getNumber())")
    @Mapping(target = "size", expression = "java(filierePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(filierePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(filierePage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toFiliereResponseList(filierePage.getContent()))")
    FilierePagingResponse toPagingResponse(Page<Filiere> filierePage);


}
