package ma.enset.noteservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import ma.enset.noteservice.exception.handler.dto.BusinessExceptionResponse;
import org.springframework.http.HttpStatus;

public class ExchangerException extends BusinessException {

    private BusinessExceptionResponse businessExceptionResponse;

    @Builder
    public ExchangerException(String exceptionBody) {
        super(exceptionBody, null);

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            businessExceptionResponse = objectMapper.readValue(exceptionBody, BusinessExceptionResponse.class);
        }catch (Exception e){
            businessExceptionResponse = BusinessExceptionResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .error("Internal Server Error")
                    .build();
        }
    }

    public BusinessExceptionResponse getBusinessExceptionResponse() {
        return businessExceptionResponse;
    }
}

