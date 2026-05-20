package br.com.psi.simulacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class SimulacaoRequest {

    @NotNull(message = "valorInicial e obrigatorio")
    @DecimalMin(value = "0.01", inclusive = true, message = "valorInicial deve ser maior que zero")
    @Digits(integer = 17, fraction = 2, message = "valorInicial deve ter ate 2 casas decimais")
    public BigDecimal valorInicial;

    @NotNull(message = "taxaJurosMensal e obrigatoria")
    @DecimalMin(value = "0.00", inclusive = true, message = "taxaJurosMensal deve ser maior ou igual a zero")
    @Digits(integer = 6, fraction = 4, message = "taxaJurosMensal deve ter ate 4 casas decimais")
    public BigDecimal taxaJurosMensal;

    @NotNull(message = "prazoMeses e obrigatorio")
    @Positive(message = "prazoMeses deve ser maior que zero")
    public Integer prazoMeses;
}
