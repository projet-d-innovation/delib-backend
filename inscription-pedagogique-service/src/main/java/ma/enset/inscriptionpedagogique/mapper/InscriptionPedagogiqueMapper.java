package ma.enset.inscriptionpedagogique.mapper;

import ma.enset.inscriptionpedagogique.dto.*;
import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface InscriptionPedagogiqueMapper {

    InscriptionPedagogique toInscriptionEntity(InscriptionCreationRequest inscriptionCreationRequest);

    InscriptionPedagogique toInscriptionEntity(RequiredSearchParams searchParams);

    List<InscriptionPedagogique> toInscriptionEntityList(List<InscriptionCreationRequest> inscriptionCreationRequestList);

    @Mapping(target = "etudiant.code", source = "inscriptionEntity.codeEtudiant")
    InscriptionResponse toInscriptionResponse(InscriptionPedagogique inscriptionEntity);

    List<InscriptionResponse> toInscriptionResponseList(List<InscriptionPedagogique> inscriptionEntityList);

    void enrichEtudiantResponse(EtudiantResponse source, @MappingTarget EtudiantResponse target);

    default void enrichEtudiantResponseList(List<EtudiantResponse> source, List<EtudiantResponse> target) {

        Map<String, EtudiantResponse> sourceEtudiantMap = source.stream()
            .collect(Collectors.toMap(EtudiantResponse::getCode, Function.identity()));

        target.forEach(etudiantResponse -> enrichEtudiantResponse(
            Objects.requireNonNull(sourceEtudiantMap.get(etudiantResponse.getCode())),
            etudiantResponse
        ));
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateInscriptionEntityFromDTO(InscriptionUpdateRequest inscriptionUpdateRequest,
                                        @MappingTarget InscriptionPedagogique inscriptionEntity);

    default void updateInscriptionEntityListFromDTOList(List<InscriptionUpdateRequest> inscriptionUpdateRequestList,
                                                        List<InscriptionPedagogique> inscriptionEntityList) {

        Map<Long, InscriptionPedagogique> inscriptionEntityMap = inscriptionEntityList.stream()
            .collect(Collectors.toMap(InscriptionPedagogique::getId, Function.identity()));

        inscriptionUpdateRequestList.forEach(inscriptionUpdateRequest -> updateInscriptionEntityFromDTO(
            inscriptionUpdateRequest,
            Objects.requireNonNull(inscriptionEntityMap.get(inscriptionUpdateRequest.id()))
        ));
    }

    @Mapping(target = "page", expression = "java(inscriptionPage.getNumber())")
    @Mapping(target = "size", expression = "java(inscriptionPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(inscriptionPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(inscriptionPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toInscriptionResponseList(inscriptionPage.getContent()))")
    InscriptionPagingResponse toPagingResponse(Page<InscriptionPedagogique> inscriptionPage);

}
