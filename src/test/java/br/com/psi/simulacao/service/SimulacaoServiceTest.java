package br.com.psi.simulacao.service;

import br.com.psi.simulacao.dto.SimulacaoRequest;
import br.com.psi.simulacao.dto.SimulacaoResponse;
import br.com.psi.simulacao.entity.SimulacaoEntity;
import br.com.psi.simulacao.exception.SimulacaoNaoEncontradaException;
import br.com.psi.simulacao.repository.SimulacaoRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulacaoServiceTest {

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarSimulacao() {
        SimulacaoRepository repository = new SimulacaoRepository() {
            @Override
            public Optional<SimulacaoEntity> findByIdOptional(Long id) {
                return Optional.empty();
            }
        };

        SimulacaoService service = new SimulacaoService(repository);

        assertThatThrownBy(() -> service.buscarPorId(123L))
                .isInstanceOf(SimulacaoNaoEncontradaException.class)
                .hasMessageContaining("123");
    }

    @Test
    void deveMapearEntidadeParaResponse() {
        SimulacaoEntity entity = new SimulacaoEntity();
        entity.id = 10L;
        entity.valorInicial = new BigDecimal("100.00");
        entity.taxaJurosMensal = new BigDecimal("2.00");
        entity.prazoMeses = 2;
        entity.valorTotalFinal = new BigDecimal("104.04");
        entity.valorTotalJuros = new BigDecimal("4.04");
        entity.criadoEm = OffsetDateTime.now();

        SimulacaoRepository repository = new SimulacaoRepository() {
            @Override
            public Optional<SimulacaoEntity> findByIdOptional(Long id) {
                return Optional.of(entity);
            }
        };

        SimulacaoService service = new SimulacaoService(repository);
        SimulacaoResponse response = service.buscarPorId(10L);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.valorInicial()).isEqualByComparingTo("100.00");
        assertThat(response.valorTotalFinal()).isEqualByComparingTo("104.04");
    }

    @Test
    void deveCalcularTotaisAoCriarNovaSimulacao() {
        SimulacaoRepository repository = new SimulacaoRepository() {
            @Override
            public void persist(SimulacaoEntity entity) {
                ((SimulacaoEntity) entity).id = 99L;
            }
        };

        SimulacaoService service = new SimulacaoService(repository);

        SimulacaoRequest request = new SimulacaoRequest();
        request.valorInicial = new BigDecimal("200.00");
        request.taxaJurosMensal = new BigDecimal("1.00");
        request.prazoMeses = 2;

        SimulacaoResponse response = service.criar(request);

        assertThat(response.id()).isEqualTo(99L);
        assertThat(response.valorTotalFinal()).isEqualByComparingTo("204.02");
        assertThat(response.valorTotalJuros()).isEqualByComparingTo("4.02");
        assertThat(response.memoriaCalculo()).hasSize(2);
    }
}
