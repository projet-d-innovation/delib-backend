package ma.enset.moduleservice.exception;

import lombok.Builder;

public class ElementNotFoundException extends BusinessException {

    @Builder
    public ElementNotFoundException(String key, Object[] args) {
        super(key, args);
    }
}
