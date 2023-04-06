package ma.enset.departementservice.exception;

import lombok.Builder;

public class DepartementNotFoundException extends BusinessException {

    @Builder
    public DepartementNotFoundException(String key, Object[] args) {
        super(key, args, "Departement not found");
    }
}
