package br.com.psi.simulacao.dto;

import java.math.BigDecimal;

public record MemoriaCalculoResponse(
        Integer mes,
        BigDecimal saldoInicial,
        BigDecimal juro,
        BigDecimal saldoFinal
) {
}
