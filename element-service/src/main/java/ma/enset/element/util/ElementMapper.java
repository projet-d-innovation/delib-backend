package ma.enset.element.util;

import ma.enset.element.dto.*;
import ma.enset.element.model.Element;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
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


    default List<ElementByCodeModuleResponse> toElementByCodeModuleResponseList(List<String>codesModule,List<List<Element>> elements){
        List<ElementByCodeModuleResponse> elementByCodeModuleResponses = new ArrayList<>();
        for (int i = 0; i < codesModule.size(); i++) {
            elementByCodeModuleResponses.add(toElementByCodeModuleResponse(codesModule.get(i),elements.get(i)));
        }
        return elementByCodeModuleResponses;
    }


    default List<ElementByCodeProfesseurResponse> toElementByCodeProfesseurResponseList(List<String>codesProfesseur,List<List<Element>> elements){
        List<ElementByCodeProfesseurResponse> elementByCodeProfesseurResponses = new ArrayList<>();
        for (int i = 0; i < codesProfesseur.size(); i++) {
            elementByCodeProfesseurResponses.add(toElementByCodeProfesseurResponse(codesProfesseur.get(i),elements.get(i)));
        }
        return elementByCodeProfesseurResponses;
    }


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

    default ElementByCodeModuleResponse toElementByCodeModuleResponse(String codeModule,List<Element> elements) {
        if (elements.isEmpty())
            return ElementByCodeModuleResponse.builder()
                    .codeModule(codeModule)
                    .elements(new ArrayList<>())
                    .build();

        return ElementByCodeModuleResponse.builder()
                .codeModule(codeModule)
                .elements(toElementResponseList(elements))
                .build();
    }

    default ElementByCodeProfesseurResponse toElementByCodeProfesseurResponse(String codeProfesseur,List<Element> elements) {
        if (elements.isEmpty())
            return ElementByCodeProfesseurResponse.builder()
                    .codeProfesseur(codeProfesseur)
                    .elements(toElementResponseList(new ArrayList<>()))
                    .build();
        return ElementByCodeProfesseurResponse.builder()
                .codeProfesseur(codeProfesseur)
                .elements(toElementResponseList(elements))
                .build();
    }

}
