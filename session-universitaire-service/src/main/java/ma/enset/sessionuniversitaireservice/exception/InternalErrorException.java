package ma.enset.sessionuniversitaireservice.exception;

import ma.enset.sessionuniversitaireservice.constant.CoreConstants;

public class InternalErrorException extends BusinessException {
    public InternalErrorException() {
        super(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR, null);
    }
}
