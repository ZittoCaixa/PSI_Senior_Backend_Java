package br.com.psi.simulacao.service;

import br.com.psi.simulacao.dto.MemoriaCalculoResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalculadoraJurosCompostosTest {

    @Test
    void deveCalcularJurosCompostosComArredondamentoMonetario() {
        CalculadoraJurosCompostos.ResultadoCalculo resultado = CalculadoraJurosCompostos.calcular(
                new BigDecimal("1000.00"),
                new BigDecimal("1.5"),
                3
        );

        assertThat(resultado.valorTotalFinal()).isEqualByComparingTo("1045.67");
        assertThat(resultado.valorTotalJuros()).isEqualByComparingTo("45.67");

        List<MemoriaCalculoResponse> memoria = resultado.memoriaCalculo();
        assertThat(memoria).hasSize(3);

        assertThat(memoria.get(0).mes()).isEqualTo(1);
        assertThat(memoria.get(0).saldoInicial()).isEqualByComparingTo("1000.00");
        assertThat(memoria.get(0).juro()).isEqualByComparingTo("15.00");
        assertThat(memoria.get(0).saldoFinal()).isEqualByComparingTo("1015.00");

        assertThat(memoria.get(1).mes()).isEqualTo(2);
        assertThat(memoria.get(1).saldoInicial()).isEqualByComparingTo("1015.00");
        assertThat(memoria.get(1).juro()).isEqualByComparingTo("15.22");
        assertThat(memoria.get(1).saldoFinal()).isEqualByComparingTo("1030.22");

        assertThat(memoria.get(2).mes()).isEqualTo(3);
        assertThat(memoria.get(2).saldoInicial()).isEqualByComparingTo("1030.22");
        assertThat(memoria.get(2).juro()).isEqualByComparingTo("15.45");
        assertThat(memoria.get(2).saldoFinal()).isEqualByComparingTo("1045.67");
    }

    @Test
    void deveRetornarMesesSemJurosQuandoTaxaForZero() {
        CalculadoraJurosCompostos.ResultadoCalculo resultado = CalculadoraJurosCompostos.calcular(
                new BigDecimal("500.00"),
                BigDecimal.ZERO,
                2
        );

        assertThat(resultado.valorTotalFinal()).isEqualByComparingTo("500.00");
        assertThat(resultado.valorTotalJuros()).isEqualByComparingTo("0.00");
        assertThat(resultado.memoriaCalculo()).hasSize(2);
        assertThat(resultado.memoriaCalculo().get(0).juro()).isEqualByComparingTo("0.00");
        assertThat(resultado.memoriaCalculo().get(1).juro()).isEqualByComparingTo("0.00");
    }
}
