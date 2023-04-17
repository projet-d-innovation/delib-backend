package ma.enset.departementservice.exception;

import ma.enset.departementservice.constant.CoreConstants;

public class InternalErrorException extends BusinessException {
    public InternalErrorException() {
        super(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR, null);
    }
}
