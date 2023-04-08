package ma.enset.filiereservice.exception;

import lombok.Builder;

public class RegleDeCalculNotFoundException extends ElementNotFoundException {
    @Builder
    public RegleDeCalculNotFoundException(String key, Object[] args) {
        super(key, args, "RegleDeCalcul not found");
    }
}
