package ma.enset.filiereservice.util;


import ma.enset.filiereservice.dto.RegleCreationRequest;
import ma.enset.filiereservice.dto.ReglePagingResponse;
import ma.enset.filiereservice.dto.RegleResponse;
import ma.enset.filiereservice.model.RegleDeCalcul;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RegleDeCalculMapper {
    RegleDeCalcul toRegleDeCalcul(RegleCreationRequest regleDeCalculCreationRequest);

    RegleResponse toRegleDeCalculResponse(RegleDeCalcul regleDeCalcul);
    List<RegleDeCalcul> toRegleDeCalculList(List<RegleCreationRequest> regleDeCalculCreationRequestList);
    List<RegleResponse> toRegleDeCalculResponseList(List<RegleDeCalcul> regleDeCalcullist);


    @Mapping(target = "page", expression = "java(regleDeCalculPage.getNumber())")
    @Mapping(target = "size", expression = "java(regleDeCalculPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(regleDeCalculPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(regleDeCalculPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    ReglePagingResponse toPagingResponse(Page<RegleDeCalcul> regleDeCalculPage);
}
