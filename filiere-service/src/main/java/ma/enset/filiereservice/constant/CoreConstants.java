package ma.enset.filiereservice.constant;

public class CoreConstants {
    public final static String VALIDATION_MESSAGE_SPLIT_DELIMITER = ":";


    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";

        public static final String RegleDeCalcule_ALREADY_EXISTS = "businessException.RegleDeCalcul.AlreadyExists.message";

        public static final String RegleDeCalcul_NOT_FOUND = "businessException.RegleDeCalcul.NotFound.message";

        public static final String RegleDeCalcul_CannotBeDeleted = "businessException.RegleDeCalcul.CannotBeDeleted.message";

        public static final String Departement_NOT_FOUND = "businessException.Departement.NotFound.message";
        public static final String Filiere_ALREADY_EXISTS = "businessException.Filiere.AlreadyExists.message";
        public static final String Filiere_NOT_FOUND = "businessException.Filiere.NotFound.message";
    }


    public static class ValidationMessage {

        public static final String PAGINATION_PAGE_MIN = "{validation.pagination.page.Min.message}";
        public static final String PAGINATION_SIZE_MIN = "{validation.pagination.size.Min.message}";
        public static final String PAGINATION_SIZE_MAX = "{validation.pagination.size.Max.message}";
    }
}
