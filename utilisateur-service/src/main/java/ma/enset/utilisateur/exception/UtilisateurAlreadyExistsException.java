package ma.enset.utilisateur.exception;

import lombok.Builder;

public class UtilisateurAlreadyExistsException extends BusinessException {
    @Builder
    public UtilisateurAlreadyExistsException(String key, Object[] args) {
        super(key, args);
    }
}
