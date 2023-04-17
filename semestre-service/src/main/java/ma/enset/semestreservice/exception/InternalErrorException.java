package ma.enset.semestreservice.exception;


import ma.enset.semestreservice.constant.CoreConstants;

public class InternalErrorException extends BusinessException {
    public InternalErrorException() {
        super(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR, null);
    }
}
