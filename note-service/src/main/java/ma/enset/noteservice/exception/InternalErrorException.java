package ma.enset.noteservice.exception;

import ma.enset.noteservice.constant.CoreConstants;

public class InternalErrorException extends BusinessException {
    public InternalErrorException() {
        super(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR, null);
    }
}
