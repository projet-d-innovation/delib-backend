


package ma.enset.filiereservice.exception;

import lombok.Builder;

public class CannotDeleteFiliereException extends BusinessException {
    @Builder
    public CannotDeleteFiliereException(String key, Object[] args) {
        super(key, args, "Cannot delete Filiere because it is used in a Semestre");
    }
}
