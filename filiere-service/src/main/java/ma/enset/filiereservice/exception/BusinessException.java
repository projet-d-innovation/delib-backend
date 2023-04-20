package ma.enset.filiereservice.exception;

public class BusinessException extends RuntimeException {

    private final String key;
    private final Object[] args;


    public BusinessException(String key, Object[] args) {
        super();
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
