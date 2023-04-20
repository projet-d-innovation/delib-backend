package ma.enset.filiereservice.util;


import ma.enset.filiereservice.dto.FiliereCreationRequest;
import ma.enset.filiereservice.dto.FilierePagingResponse;
import ma.enset.filiereservice.dto.FiliereResponse;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.model.RegleDeCalcul;
import ma.enset.filiereservice.service.RegleDeCalculService;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface FiliereMapper {

    @Mapping(target = "codeChefFiliere", expression = "java(filiereCreationRequest.codeChefFiliere())")
    @Mapping(target = "regleDeCalcul", expression = "java(mapToRegleDeCalcul(filiereCreationRequest.codeRegleDeCalcul() , regleDeCalculService))")
    Filiere toFiliere(FiliereCreationRequest filiereCreationRequest, RegleDeCalculService regleDeCalculService);

    @Mapping(target = "codeRegle", expression = "java(filiere.getRegleDeCalcul().getCodeRegle())")
    FiliereResponse toFiliereResponse(Filiere filiere);


    List<FiliereResponse> toFiliereResponseList(List<Filiere> filierelist);


    @Mapping(target = "page", expression = "java(filierePage.getNumber())")
    @Mapping(target = "size", expression = "java(filierePage.getSize())")
    @Mapping(target = "totalPages", expression = "java(filierePage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(filierePage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    FilierePagingResponse toPagingResponse(Page<Filiere> filierePage);

    default RegleDeCalcul mapToRegleDeCalcul(String codeRegleDeCalcul, RegleDeCalculService regleDeCalculService) {
        if (codeRegleDeCalcul == null) {
            return null;
        }
        return regleDeCalculService.findByCodeRegleDeCalcul(codeRegleDeCalcul);
    }

    default List<Filiere> toFiliereList(List<FiliereCreationRequest> filiereCreationRequestList , RegleDeCalculService regleDeCalculService) {
        List<Filiere> filieres = new ArrayList<>();
        for (FiliereCreationRequest filiereCreationRequest : filiereCreationRequestList) {
            Filiere filiere = toFiliere(filiereCreationRequest , regleDeCalculService);
            filieres.add(filiere);
        }
        return filieres;
    }




}
