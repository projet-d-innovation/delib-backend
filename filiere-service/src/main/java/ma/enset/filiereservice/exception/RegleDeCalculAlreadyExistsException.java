package ma.enset.filiereservice.exception;

        import lombok.Builder;

public class RegleDeCalculAlreadyExistsException extends BusinessException {
    @Builder
    public RegleDeCalculAlreadyExistsException(String key, Object[] args) {
        super(key, args, "RegleDeCalcul already exists");
    }
}
