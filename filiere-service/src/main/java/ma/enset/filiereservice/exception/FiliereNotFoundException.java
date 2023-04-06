package ma.enset.filiereservice.exception;

import lombok.Builder;

public class FiliereNotFoundException extends BusinessException {

    @Builder
    public FiliereNotFoundException(String key, Object[] args) {
        super(key, args, "Filiere not found");
    }
}