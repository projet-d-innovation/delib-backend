package ma.enset.utilisateur.exception;

import lombok.Builder;

public class UtilisateurNotFoundException extends BusinessException {

    @Builder
    public UtilisateurNotFoundException(String key, Object[] args) {
        super(key, args);
    }
}
