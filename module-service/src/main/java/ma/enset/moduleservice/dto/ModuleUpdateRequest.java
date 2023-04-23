package ma.enset.moduleservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ModuleUpdateRequest(
    String intituleModule,

    @DecimalMin("0.1")
    @DecimalMax("1")
    BigDecimal coefficientModule
) {}
