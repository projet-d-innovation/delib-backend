package ma.enset.inscriptionpedagogique.exception.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ExceptionResponse {

    private int code;
    private String status;
    private String message;
    private List<String> identifiers;
    private List<ValidationError> errors;


    @Builder
    public record ValidationError (
        String field,
        String message

    ) { }

}
