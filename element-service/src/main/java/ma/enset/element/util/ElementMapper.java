package ma.enset.element.util;

import ma.enset.element.dto.*;
import ma.enset.element.model.Element;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ElementMapper {

    Element toElement(ElementCreationRequest elementCreationRequest);

    @Mapping(
            target = "coefficientElement",
            expression = "java(new java.math.BigDecimal( String.valueOf(element.getCoefficientElement())) )"
    )
    ElementResponse toElementResponse(Element element);

    List<Element> toElementList(List<ElementCreationRequest> elementCreationRequestList);


    List<Element> toElementListUpdate(List<ElementUpdateRequest> elementUpdateRequestList);

    List<ElementResponse> toElementResponseList(List<Element> elementList);


    List<ElementByCodeModuleResponse> toElementByCodeModuleResponseList(List<List<Element>> elements);


    List<ElementByCodeProfesseurResponse> toElementByCodeProfesseurResponseList(List<List<Element>> elements);


    List<String> toCodeElementList(List<ElementUpdateRequest> elementUpdateRequestList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateElementFromDTO(ElementUpdateRequest elementUpdateRequest, @MappingTarget Element element);


    default void updateElementFromDTO(List<ElementUpdateRequest> elementUpdateRequest, List<Element> element) {
        for (int i = 0; i < element.size(); i++) {
            updateElementFromDTO(elementUpdateRequest.get(i), element.get(i));
        }
    }

    @Mapping(target = "page", expression = "java(elementPage.getNumber())")
    @Mapping(target = "size", expression = "java(elementPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(elementPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(elementPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    ElementPagingResponse toPagingResponse(Page<Element> elementPage);


    default String toCodeElement(ElementUpdateRequest elementUpdateRequest) {
        return elementUpdateRequest.codeElement();
    }

    default ElementByCodeModuleResponse toElementByCodeModuleResponse(List<Element> elements) {
        if (elements.isEmpty()) return null; // TODO : DELETE THIS AFTER IMPLEMENTING MODULE NOT FOUND EXCEPTION
        return ElementByCodeModuleResponse.builder()
                .codeModule(elements.get(0).getCodeModule())
                .elements(toElementResponseList(elements))
                .build();
    }

    default ElementByCodeProfesseurResponse toElementByCodeProfesseurResponse(List<Element> elements) {
        if (elements.isEmpty()) return null; // TODO : DELETE THIS AFTER IMPLEMENTING PROFESSEUR NOT FOUND EXCEPTION
        return ElementByCodeProfesseurResponse.builder()
                .codeProfesseur(elements.get(0).getCodeProfesseur())
                .elements(toElementResponseList(elements))
                .build();
    }

}
