package ma.enset.departementservice.exception;

import lombok.Builder;

public class DepartementAlreadyExistsException extends BusinessException {
    @Builder
    public DepartementAlreadyExistsException(String key, Object[] args) {
        super(key, args, "Departement already exists");
    }
}
