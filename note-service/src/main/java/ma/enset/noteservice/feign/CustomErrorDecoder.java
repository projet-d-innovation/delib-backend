package ma.enset.noteservice.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.Data;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.exception.BusinessException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.exception.handler.dto.BusinessExceptionResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        BusinessExceptionResponse message = null;
        try (
                InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, BusinessExceptionResponse.class);
        } catch (IOException e) {
            return new Exception(e);
        }

        return switch (response.status()) {
            case 400 -> new BadRequestException(message.error());
            case 404 -> {
                if (response.request().url().split("/")[response.request().url().split("/").length - 2].equals("modules"))
                    yield ElementNotFoundException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.CLIENT_EXEPTION)
                            .args(new Object[]{message.error()})
                            .build();
                else if (response.request().url().split("/")[response.request().url().split("/").length - 2].equals("elements"))
                    yield ElementNotFoundException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.CLIENT_EXEPTION)
                            .args(new Object[]{message.error()})
                            .build();
                else
                yield  new InternalServerErrorException("Internal server error");
            }
            default -> new InternalServerErrorException("Internal server error");
        };
    }
}
