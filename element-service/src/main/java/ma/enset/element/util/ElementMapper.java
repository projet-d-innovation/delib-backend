package ma.enset.element.util;

import ma.enset.element.dto.ElementRequestDTO;
import ma.enset.element.dto.ElementResponseDTO;
import ma.enset.element.model.Element;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ElementMapper {
    Element toElement(ElementRequestDTO productRequest);

    List<Element> toElements(List<ElementRequestDTO> elementRequests);

    ElementResponseDTO toElementResponse(Element product);

    List<ElementResponseDTO> toElementResponses(List<Element> products);
}
