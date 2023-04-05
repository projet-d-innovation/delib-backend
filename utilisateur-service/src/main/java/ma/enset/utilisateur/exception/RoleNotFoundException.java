package ma.enset.utilisateur.exception;

import lombok.Builder;

public class RoleNotFoundException extends BusinessException {

    @Builder
    public RoleNotFoundException(String key, Object[] args) {
        super(key, args);
    }
}
