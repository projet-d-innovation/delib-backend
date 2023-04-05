package ma.enset.utilisateur.exception;

import lombok.Builder;

public class PermissionAlreadyExistsException extends BusinessException {
    @Builder
    public PermissionAlreadyExistsException(String key, Object[] args) {
        super(key, args);
    }
}
