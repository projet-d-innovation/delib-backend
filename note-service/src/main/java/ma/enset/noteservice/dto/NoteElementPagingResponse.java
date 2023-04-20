package ma.enset.noteservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NoteElementPagingResponse(
    int page,
    int size,
    int totalPages,
    int totalElements,
    List<NoteElementWithElementResponse> records
) {
    public void setElementResponseList(List<ElementResponse> body) {
        for (int i = 0; i < records.size(); i++) {
            for (int j = 0; j < body.size(); j++) {
                if (this.records.get(i).codeElement().equals(body.get(j).codeElement())) {
                   this.records.set(i, records.get(i).setElementResponse(body.get(j)));
                }
            }
        }
//        return this;
    }
}
