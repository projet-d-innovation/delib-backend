package ma.enset.utilisateur.exception;

import lombok.Builder;

public class RoleConflictException extends BusinessException {

    @Builder
    public RoleConflictException(String key, Object[] args) {
        super(key, args);
    }
}
