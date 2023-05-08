package ma.enset.sessionuniversitaireservice.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ElementAlreadyExistsException extends BusinessException {
    private final List<String> identifiers;

    public ElementAlreadyExistsException(String key, Object[] args, List<String> identifiers) {
        super(key, args);
        this.identifiers = identifiers;
    }
}
