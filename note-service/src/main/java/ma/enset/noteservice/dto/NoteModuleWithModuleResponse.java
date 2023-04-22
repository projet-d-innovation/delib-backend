package ma.enset.noteservice.dto;

import ma.enset.noteservice.enums.Resultat;

public record NoteModuleWithModuleResponse(
    String noteModuleId,
    float note,
    Resultat resultat,
    boolean redoublant,
    boolean rattrapage,
    String codeModule,

    ModuleResponse module,
    String codeSession
) {
    public NoteModuleWithModuleResponse setElementResponse(ModuleResponse module) {
        return new NoteModuleWithModuleResponse(this.noteModuleId(), this.note(), this.resultat(), this.redoublant(), this.rattrapage(), this.codeModule(), module, this.codeSession());
    }
}
