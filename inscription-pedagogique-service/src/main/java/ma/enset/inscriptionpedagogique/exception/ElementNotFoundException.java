package ma.enset.inscriptionpedagogique.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ElementNotFoundException extends BusinessException {
    private final List<String> identifiers;

    public ElementNotFoundException(String key, Object[] args, List<String> identifiers) {
        super(key, args);
        this.identifiers = identifiers;
    }
}
