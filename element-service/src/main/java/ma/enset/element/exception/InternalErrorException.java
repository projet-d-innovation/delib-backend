package ma.enset.element.exception;

import ma.enset.element.constant.CoreConstants;

public class InternalErrorException extends BusinessException {
    public InternalErrorException() {
        super(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR, null);
    }
}

