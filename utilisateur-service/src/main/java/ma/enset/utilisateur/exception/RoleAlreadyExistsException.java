package ma.enset.utilisateur.exception;

import lombok.Builder;

public class RoleAlreadyExistsException extends BusinessException {
    @Builder
    public RoleAlreadyExistsException(String key, Object[] args) {
        super(key, args);
    }
}
