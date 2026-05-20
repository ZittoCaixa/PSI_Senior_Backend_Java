package br.com.psi.simulacao.exception;

public class SimulacaoNaoEncontradaException extends RuntimeException {

    public SimulacaoNaoEncontradaException(Long id) {
        super("Simulacao com ID " + id + " nao encontrada.");
    }
}
