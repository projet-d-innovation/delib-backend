package ma.enset.semestreservice.exception;

import lombok.Builder;

public class SemestreAlreadyExistsException extends BusinessException {
    @Builder
    public SemestreAlreadyExistsException(String key, Object[] args) {
        super(key, args, "Semestre already exists");
    }
}
