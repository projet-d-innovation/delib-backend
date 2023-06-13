package ma.enset.filiereservice.mapper;


import ma.enset.filiereservice.dto.RegleDeCalculCreationRequest;
import ma.enset.filiereservice.dto.RegleDeCalculPagingResponse;
import ma.enset.filiereservice.dto.RegleDeCalculResponse;
import ma.enset.filiereservice.dto.RegleDeCalculUpdateRequest;
import ma.enset.filiereservice.model.RegleDeCalcul;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RegleDeCalculMapper {
    RegleDeCalcul toRegleDeCalcul(RegleDeCalculCreationRequest regleDeCalculCreationRequest);


    @Mapping(
            target = "noteValidationModule",
            expression = "java(new java.math.BigDecimal(String.valueOf(regleDeCalcul.getNoteValidationModule())))"
    )
    @Mapping(
            target = "noteEliminatoireModule",
            expression = "java(new java.math.BigDecimal(String.valueOf(regleDeCalcul.getNoteEliminatoireModule())))"
    )
    @Mapping(
            target = "noteCompensationModule",
            expression = "java(new java.math.BigDecimal(String.valueOf(regleDeCalcul.getNoteCompensationModule())))"
    )
    @Mapping(
            target = "noteValidationAnnee",
            expression = "java(new java.math.BigDecimal(String.valueOf(regleDeCalcul.getNoteValidationAnnee())))"
    )
    RegleDeCalculResponse toRegleDeCalculResponse(RegleDeCalcul regleDeCalcul);

    List<RegleDeCalcul> toRegleDeCalculList(List<RegleDeCalculCreationRequest> regleDeCalculCreationRequestList);

    List<RegleDeCalculResponse> toRegleDeCalculResponseList(List<RegleDeCalcul> regleDeCalcullist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRegleDeCalculFromDto(RegleDeCalculUpdateRequest regleDeCalculUpdateRequest, @MappingTarget RegleDeCalcul regleDeCalcul);

    @Mapping(target = "page", expression = "java(regleDeCalculPage.getNumber())")
    @Mapping(target = "size", expression = "java(regleDeCalculPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(regleDeCalculPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(regleDeCalculPage.getTotalElements())")
    @Mapping(target = "records", expression = "java(toRegleDeCalculResponseList(regleDeCalculPage.getContent()))")
    RegleDeCalculPagingResponse toPagingResponse(Page<RegleDeCalcul> regleDeCalculPage);
}
