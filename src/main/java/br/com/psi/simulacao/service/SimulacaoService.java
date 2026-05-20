package br.com.psi.simulacao.service;

import br.com.psi.simulacao.dto.MemoriaCalculoResponse;
import br.com.psi.simulacao.dto.SimulacaoRequest;
import br.com.psi.simulacao.dto.SimulacaoResponse;
import br.com.psi.simulacao.entity.MemoriaCalculoEntity;
import br.com.psi.simulacao.entity.SimulacaoEntity;
import br.com.psi.simulacao.exception.SimulacaoNaoEncontradaException;
import br.com.psi.simulacao.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    private final SimulacaoRepository repository;

    public SimulacaoService(SimulacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SimulacaoResponse criar(SimulacaoRequest request) {
        CalculadoraJurosCompostos.ResultadoCalculo resultado = CalculadoraJurosCompostos.calcular(
                request.valorInicial,
                request.taxaJurosMensal,
                request.prazoMeses
        );

        SimulacaoEntity simulacao = new SimulacaoEntity();
        simulacao.valorInicial = request.valorInicial;
        simulacao.taxaJurosMensal = request.taxaJurosMensal;
        simulacao.prazoMeses = request.prazoMeses;
        simulacao.valorTotalFinal = resultado.valorTotalFinal();
        simulacao.valorTotalJuros = resultado.valorTotalJuros();
        simulacao.criadoEm = OffsetDateTime.now();

        for (MemoriaCalculoResponse item : resultado.memoriaCalculo()) {
            MemoriaCalculoEntity memoria = new MemoriaCalculoEntity();
            memoria.mes = item.mes();
            memoria.saldoInicial = item.saldoInicial();
            memoria.juroMes = item.juro();
            memoria.saldoFinal = item.saldoFinal();
            memoria.simulacao = simulacao;
            simulacao.memoriaCalculo.add(memoria);
        }

        repository.persist(simulacao);
        return toResponse(simulacao);
    }

    public SimulacaoResponse buscarPorId(Long id) {
        SimulacaoEntity simulacao = repository.findByIdOptional(id)
                .orElseThrow(() -> new SimulacaoNaoEncontradaException(id));

        return toResponse(simulacao);
    }

    private SimulacaoResponse toResponse(SimulacaoEntity simulacao) {
        List<MemoriaCalculoResponse> memoria = simulacao.memoriaCalculo.stream()
                .sorted(Comparator.comparing(item -> item.mes))
                .map(item -> new MemoriaCalculoResponse(
                        item.mes,
                        item.saldoInicial,
                        item.juroMes,
                        item.saldoFinal
                ))
                .toList();

        return new SimulacaoResponse(
                simulacao.id,
                simulacao.valorInicial,
                simulacao.taxaJurosMensal,
                simulacao.prazoMeses,
                simulacao.valorTotalFinal,
                simulacao.valorTotalJuros,
                simulacao.criadoEm,
                memoria
        );
    }
}
