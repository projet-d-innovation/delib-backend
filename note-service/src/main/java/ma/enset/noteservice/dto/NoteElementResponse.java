package ma.enset.noteservice.dto;


public record NoteElementResponse(
    String NoteElementId,
    float note,
    boolean redoublant
//    String codeSemestre
) { }
