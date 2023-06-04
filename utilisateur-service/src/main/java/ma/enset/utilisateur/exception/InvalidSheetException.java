package ma.enset.utilisateur.exception;

public class InvalidSheetException extends BusinessException {
    public InvalidSheetException(String key, Object[] args) {
        super(key, args);
    }
}
