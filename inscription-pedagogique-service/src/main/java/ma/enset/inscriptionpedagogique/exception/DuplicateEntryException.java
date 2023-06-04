package ma.enset.inscriptionpedagogique.exception;

import lombok.Getter;

@Getter
public class DuplicateEntryException extends BusinessException {
    public DuplicateEntryException(String key, Object[] args) {
        super(key, args);
    }
}
