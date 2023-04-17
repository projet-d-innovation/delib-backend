package ma.enset.semestreservice.exception;

import lombok.Builder;

public class ElementAlreadyExistsException extends BusinessException {
    @Builder
    public ElementAlreadyExistsException(String key, Object[] args) {
        super(key, args);
    }
}
