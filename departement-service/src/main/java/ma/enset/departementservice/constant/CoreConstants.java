package ma.enset.departementservice.constant;

public class CoreConstants {
    public final static String VALIDATION_MESSAGE_SPLIT_DELIMITER = ":";


    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String ELEMENT_NOT_FOUND = "businessException.element.NotFound.message";
        public static final String ELEMENT_ALREADY_EXISTS = "businessException.element.AlreadyExists.message";

        public static final String ELEMENT_ALREADY_USED_IN_ANOTHER_ENTITY = "businessException.element.AlreadyUsedInAnotherEntity.message";
    }


    public static class ValidationMessage {

        public static final String PAGINATION_PAGE_MIN = "{validation.pagination.page.Min.message}";
        public static final String PAGINATION_SIZE_MIN = "{validation.pagination.size.Min.message}";
        public static final String PAGINATION_SIZE_MAX = "{validation.pagination.size.Max.message}";
    }
}
