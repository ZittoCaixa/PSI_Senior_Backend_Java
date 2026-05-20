package br.com.psi.simulacao.service;

import br.com.psi.simulacao.dto.MemoriaCalculoResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class CalculadoraJurosCompostos {

    private static final int ESCALA_MONETARIA = 2;
    private static final int ESCALA_TAXA = 10;
    private static final BigDecimal CEM = new BigDecimal("100");

    private CalculadoraJurosCompostos() {
    }

    public static ResultadoCalculo calcular(BigDecimal valorInicial, BigDecimal taxaJurosMensal, int prazoMeses) {
        BigDecimal taxaDecimal = taxaJurosMensal.divide(CEM, ESCALA_TAXA, RoundingMode.HALF_EVEN);
        BigDecimal saldoAtual = valorInicial.setScale(ESCALA_MONETARIA, RoundingMode.HALF_EVEN);
        List<MemoriaCalculoResponse> memoria = new ArrayList<>();

        for (int mes = 1; mes <= prazoMeses; mes++) {
            BigDecimal saldoInicialMes = saldoAtual;
            BigDecimal juroMes = saldoInicialMes.multiply(taxaDecimal).setScale(ESCALA_MONETARIA, RoundingMode.HALF_EVEN);
            BigDecimal saldoFinalMes = saldoInicialMes.add(juroMes).setScale(ESCALA_MONETARIA, RoundingMode.HALF_EVEN);

            memoria.add(new MemoriaCalculoResponse(
                    mes,
                    saldoInicialMes,
                    juroMes,
                    saldoFinalMes
            ));

            saldoAtual = saldoFinalMes;
        }

        BigDecimal valorTotalFinal = saldoAtual.setScale(ESCALA_MONETARIA, RoundingMode.HALF_EVEN);
        BigDecimal valorTotalJuros = valorTotalFinal.subtract(valorInicial).setScale(ESCALA_MONETARIA, RoundingMode.HALF_EVEN);

        return new ResultadoCalculo(valorTotalFinal, valorTotalJuros, memoria);
    }

    public record ResultadoCalculo(
            BigDecimal valorTotalFinal,
            BigDecimal valorTotalJuros,
            List<MemoriaCalculoResponse> memoriaCalculo
    ) {
    }
}
