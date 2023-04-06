package ma.enset.semestreservice.exception;

import lombok.Builder;

public class SemestreNotFoundException extends BusinessException {

    @Builder
    public SemestreNotFoundException(String key, Object[] args) {
        super(key, args, "Semestre not found");
    }
}
