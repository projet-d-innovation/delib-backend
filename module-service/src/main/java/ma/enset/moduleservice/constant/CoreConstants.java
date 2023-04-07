package ma.enset.moduleservice.constant;

public class CoreConstants {
    public final static String VALIDATION_MESSAGE_SPLIT_DELIMITER = ":";

    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String MODULE_NOT_FOUND = "businessException.module.NotFound.message";
        public static final String MODULE_ALREADY_EXISTS = "businessException.module.AlreadyExists.message";
    }

    public static class ValidationMessage {
        public static final String PAGING_PAGE_MIN = "{validation.paging.page.Min.message}";
        public static final String PAGING_SIZE_RANGE = "{validation.paging.size.Range.message}";
    }
}
