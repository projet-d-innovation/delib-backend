
package ma.enset.filiereservice.exception;

import lombok.Builder;

public class CannotDeleteRegleDeCalculException extends BusinessException {
    @Builder
    public CannotDeleteRegleDeCalculException(String key, Object[] args) {
        super(key, args, "Cannot delete RegleDeCalcul because it is used in a Filiere");
    }
}
