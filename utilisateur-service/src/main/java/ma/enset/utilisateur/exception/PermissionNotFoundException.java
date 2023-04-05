package ma.enset.utilisateur.exception;

import lombok.Builder;

public class PermissionNotFoundException extends BusinessException {

    @Builder
    public PermissionNotFoundException(String key, Object[] args) {
        super(key, args);
    }
}
