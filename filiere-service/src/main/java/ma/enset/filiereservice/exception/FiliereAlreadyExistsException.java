package ma.enset.filiereservice.exception;

import lombok.Builder;

public class FiliereAlreadyExistsException extends BusinessException {
    @Builder
    public FiliereAlreadyExistsException(String key, Object[] args) {
        super(key, args, "Filiere already exists");
    }
}
