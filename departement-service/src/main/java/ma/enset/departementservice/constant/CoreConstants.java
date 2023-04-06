package ma.enset.departementservice.constant;

public class CoreConstants {
    public final static String VALIDATION_MESSAGE_SPLIT_DELIMITER = ":";


    // We use constants as a convenient way instead of specifying the whole key
    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String ELEMENT_NOT_FOUND = "businessException.element.NotFound.message";
        public static final String ELEMENT_ALREADY_EXISTS = "businessException.element.AlreadyExists.message";
    }


    public static class ValidationMessage {
        // Keys are wrapped in `{}` because they are used as hibernate validator's constraints' Message descriptors' parameters
        // which will be resolved during message interpolation
        // see: https://docs.jboss.org/hibernate/validator/8.0/reference/en-US/html_single/#section-message-interpolation
        public static final String PAGINATION_PAGE_MIN = "{validation.pagination.page.Min.message}";
        public static final String PAGINATION_SIZE_MIN = "{validation.pagination.size.Min.message}";
        public static final String PAGINATION_SIZE_MAX = "{validation.pagination.size.Max.message}";
    }
}
