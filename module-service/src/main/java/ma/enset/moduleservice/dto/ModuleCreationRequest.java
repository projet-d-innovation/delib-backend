package ma.enset.moduleservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ModuleCreationRequest(
    @NotBlank
    String codeModule,

    @NotBlank
    String intituleModule,

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("1")
    BigDecimal coefficientModule,

    @NotBlank
    String codeSemestre
) {}
