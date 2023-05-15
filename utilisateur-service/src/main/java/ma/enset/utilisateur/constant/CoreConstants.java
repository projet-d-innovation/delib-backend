package ma.enset.utilisateur.constant;

public class CoreConstants {


    public static class RoleID {
        public static final String ROLE_ADMIN = "ADMIN";
        public static final String ROLE_PROFESSEUR = "PROFESSEUR";

        public static final String ROLE_ETUDIANT = "ETUDIANT";
    }

    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String NOT_FOUND = "businessException.NotFound.message";
        public static final String ALREADY_EXISTS = "businessException.AlreadyExists.message";
        public static final String MANY_NOT_FOUND = "businessException.many.NotFound.message";
        public static final String MANY_ALREADY_EXISTS = "businessException.many.AlreadyExists.message";
        public static final String DUPLICATE_ENTRY = "businessException.DuplicateEntry.message";
    }


}
