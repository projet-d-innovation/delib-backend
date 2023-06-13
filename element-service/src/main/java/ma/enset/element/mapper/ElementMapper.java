package ma.enset.element.mapper;

import ma.enset.element.dto.ElementCreationRequest;
import ma.enset.element.dto.ElementPagingResponse;
import ma.enset.element.dto.ElementResponse;
import ma.enset.element.dto.ElementUpdateRequest;
import ma.enset.element.model.Element;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ElementMapper {

    Element toElement(ElementCreationRequest request);

    @Mapping(
            target = "coefficientElement",
            expression = "java(new java.math.BigDecimal( String.valueOf(element.getCoefficientElement())) )"
    )
    ElementResponse toElementResponse(Element element);

    List<Element> toElementList(List<ElementCreationRequest> request);

    List<ElementResponse> toElementResponseList(List<Element> elements);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "codeProfesseur", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateElementFromDTO(ElementUpdateRequest request, @MappingTarget Element element);

    @Mapping(target = "page", expression = "java(elementPage.getNumber())")
    @Mapping(target = "size", expression = "java(elementPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(elementPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(elementPage.getTotalElements())")
    @Mapping(target = "records", expression = "java(toElementResponseList(elementPage.getContent()))")
    ElementPagingResponse toPagingResponse(Page<Element> elementPage);
}
