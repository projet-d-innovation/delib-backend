package ma.enset.noteservice.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.exception.ElementNotFoundException;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new BadRequestException("Bad request");
            case 404 -> {
                if (response.request().url().split("/")[response.request().url().split("/").length - 2].equals("modules"))
                    yield ElementNotFoundException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.MODULE_NOT_FOUND)
                            .args(new Object[]{response.request().url().split("/")[response.request().url().split("/").length - 1]})
                            .build();
                else if (response.request().url().split("/")[response.request().url().split("/").length - 2].equals("elements"))
                    yield ElementNotFoundException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                            .args(new Object[]{response.request().url().split("/")[response.request().url().split("/").length - 1]})
                            .build();
                yield new Exception("Data not found");

            }
            case 500 -> new Exception("Internal server error");
            default -> new Exception("Generic error");
        };
    }
}
