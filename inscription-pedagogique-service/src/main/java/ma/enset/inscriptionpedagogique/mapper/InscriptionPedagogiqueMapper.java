package ma.enset.inscriptionpedagogique.mapper;

import ma.enset.inscriptionpedagogique.dto.EtudiantResponse;
import ma.enset.inscriptionpedagogique.dto.InscriptionCreationRequest;
import ma.enset.inscriptionpedagogique.dto.InscriptionResponse;
import ma.enset.inscriptionpedagogique.dto.InscriptionUpdateRequest;
import ma.enset.inscriptionpedagogique.model.InscriptionPedagogique;
import org.mapstruct.*;

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

    List<InscriptionPedagogique> toInscriptionEntityList(List<InscriptionCreationRequest> inscriptionCreationRequestList);

    InscriptionResponse toInscriptionResponse(InscriptionPedagogique inscriptionEntity, EtudiantResponse etudiant);

    default List<InscriptionResponse> toInscriptionResponseList(List<InscriptionPedagogique> inscriptionEntityList,
                                                                List<EtudiantResponse> etudiantList) {

        Map<String, EtudiantResponse> etudiantMap = etudiantList.stream()
            .collect(Collectors.toMap(EtudiantResponse::code, Function.identity()));

        return inscriptionEntityList.stream()
            .map(inscriptionEntity -> toInscriptionResponse(
                inscriptionEntity,
                etudiantMap.get(inscriptionEntity.getCodeEtudiant()))
            )
            .toList();
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

}
