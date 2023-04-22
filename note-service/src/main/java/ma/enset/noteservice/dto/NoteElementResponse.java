package ma.enset.noteservice.dto;


public record NoteElementResponse(
    String noteElementId,
    float note,
    boolean redoublant,
    String codeElement,
    String codeSession
) {


}
