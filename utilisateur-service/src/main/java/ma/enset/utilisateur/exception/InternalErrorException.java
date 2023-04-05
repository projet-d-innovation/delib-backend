package ma.enset.utilisateur.exception;

import lombok.Builder;

public class InternalErrorException extends BusinessException {

    @Builder
    public InternalErrorException(String key, Object[] args) {
        super(key, args);
    }
}
