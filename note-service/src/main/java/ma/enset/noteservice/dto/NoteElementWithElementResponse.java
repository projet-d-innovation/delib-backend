package ma.enset.noteservice.dto;


public record NoteElementWithElementResponse(
    String noteElementId,
    float note,
    boolean redoublant,

    String codeElement,
    ElementResponse element
) {

    public NoteElementWithElementResponse setElementResponse(ElementResponse element) {
        return new NoteElementWithElementResponse(this.noteElementId(), this.note(), this.redoublant(), this.codeElement(),  element);
    }

}