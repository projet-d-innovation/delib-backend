package ma.enset.utilisateur.constant;

public class CoreConstants {


    public static class RoleID{
        public static final String ROLE_ADMIN = "ADMIN";
        public static final String ROLE_PROFESSEUR = "PROFESSEUR";

        public static final String ROLE_ETUDIANT = "ETUDIANT";
    }

    public static class BusinessExceptionMessage {

        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String UTILISATEUR_NOT_FOUND = "businessException.utilisateur.NotFound.message";
        public static final String UTILISATEUR_ALREADY_EXISTS = "businessException.utilisateur.AlreadyExists.message";

        public static final String ROLE_NOT_FOUND = "businessException.role.NotFound.message";
        public static final String ROLE_ALREADY_EXISTS = "businessException.role.AlreadyExists.message";

        public static final String PERMISSION_NOT_FOUND = "businessException.permission.NotFound.message";
        public static final String PERMISSION_ALREADY_EXISTS = "businessException.permission.AlreadyExists.message";
    }


}
