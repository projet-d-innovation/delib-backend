package ma.enset.semestreservice.constant;

public class CoreConstants {
    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String SEMESTRE_NOT_FOUND = "businessException.semestre.NotFound.message";
        public static final String SEMESTRE_ALREADY_EXISTS = "businessException.semestre.AlreadyExists.message";

        public static final String FILIERE_NOT_FOUND = "businessException.filiere.NotFound.message";

        public static final String MODULE_NOT_FOUND = "businessException.module.NotFound.message";
        public static final String MODULE_ALREADY_EXISTS = "businessException.module.AlreadyExists.message";

        public static final String SESSION_NOT_FOUND = "businessException.session.NotFound.message";
        public static final String SESSION_ALREADY_EXISTS = "businessException.session.AlreadyExists.message";
    }
}
