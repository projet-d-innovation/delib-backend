package ma.enset.filiereservice.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String key;
    private final Object[] args;

    public BusinessException(String key, Object[] args) {
        super();
        this.key = key;
        this.args = args;
    }
}
