package ma.enset.departementservice.mapper;


import ma.enset.departementservice.dto.DepartementCreationRequest;
import ma.enset.departementservice.dto.DepartementPagingResponse;
import ma.enset.departementservice.dto.DepartementResponse;
import ma.enset.departementservice.dto.DepartementUpdateRequest;
import ma.enset.departementservice.model.Departement;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DepartementMapper {
    Departement toDepartement(DepartementCreationRequest departementCreationRequest);

    DepartementResponse toDepartementResponse(Departement departement);
    List<Departement> toDepartementList(List<DepartementCreationRequest> departementCreationRequestList);
    List<DepartementResponse> toDepartementResponseList(List<Departement> departementlist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartementFromDTO(DepartementUpdateRequest departementUpdateRequest, @MappingTarget Departement departement);

    @Mapping(target = "page", expression = "java(departementPage.getNumber())")
    @Mapping(target = "size", expression = "java(departementPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(departementPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(departementPage.getTotalElements())")
    @Mapping(source = "content", target = "records")
    DepartementPagingResponse toPagingResponse(Page<Departement> departementPage);

}
