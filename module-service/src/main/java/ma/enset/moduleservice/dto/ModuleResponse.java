package ma.enset.moduleservice.dto;

import java.math.BigDecimal;

public record ModuleResponse(
    String codeModule,
    String intituleModule,
    BigDecimal coefficientModule,
    String codeSemestre
) { }
