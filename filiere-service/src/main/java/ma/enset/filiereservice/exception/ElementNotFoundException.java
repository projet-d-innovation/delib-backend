package ma.enset.filiereservice.exception;

public class ElementNotFoundException extends BusinessException{
    public ElementNotFoundException(String key, Object[] args, String message) {
        super(key, args, message);
    }
}
