package ma.enset.utilisateur.constant;

public class CoreConstants {
    public final static String VALIDATION_MESSAGE_SPLIT_DELIMITER = ":";


    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String UTILISATEUR_NOT_FOUND = "businessException.utilisateur.NotFound.message";
        public static final String UTILISATEUR_ALREADY_EXISTS = "businessException.utilisateur.AlreadyExists.message";

        public static final String ROLE_NOT_FOUND = "businessException.role.NotFound.message";
        public static final String ROLE_ALREADY_EXISTS = "businessException.role.AlreadyExists.message";
        public static final String ROLE_CONFLICT = "businessException.role.Conflict.message";

        public static final String PERMISSION_NOT_FOUND = "businessException.permission.NotFound.message";
        public static final String PERMISSION_ALREADY_EXISTS = "businessException.permission.AlreadyExists.message";
    }


    public static class ValidationMessage {
        public static final String PAGINATION_PAGE_MIN = "{validation.pagination.page.Min.message}";
        public static final String PAGINATION_SIZE_MIN = "{validation.pagination.size.Min.message}";
        public static final String PAGINATION_SIZE_MAX = "{validation.pagination.size.Max.message}";
    }
}
