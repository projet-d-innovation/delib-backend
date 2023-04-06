package ma.enset.semestreservice.exception;

public class BusinessException extends RuntimeException {

    private final String key;
    private final Object[] args;


    public BusinessException(String key, Object[] args ,String message) {
        super(message);
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public Object[] getArgs() {
        return args;
    }
}
