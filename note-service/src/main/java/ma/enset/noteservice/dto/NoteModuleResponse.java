package ma.enset.noteservice.dto;

import ma.enset.noteservice.enums.Resultat;

import javax.xml.transform.Result;

public record NoteModuleResponse(
    String noteModuleId,
    float note,
    Resultat resultat,
    boolean redoublant,
    boolean rattrapage,
    String codeModule,
    String codeSession

) {

}
