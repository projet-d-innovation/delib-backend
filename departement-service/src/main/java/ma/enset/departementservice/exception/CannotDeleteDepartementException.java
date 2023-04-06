package ma.enset.departementservice.exception;

import lombok.Builder;

public class CannotDeleteDepartementException extends BusinessException {
    @Builder
    public CannotDeleteDepartementException(String key, Object[] args) {
        super(key, args, "Departement cannot be deleted");
    }
}
