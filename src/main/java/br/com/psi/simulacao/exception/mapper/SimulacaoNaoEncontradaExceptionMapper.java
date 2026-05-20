package br.com.psi.simulacao.exception.mapper;

import br.com.psi.simulacao.exception.SimulacaoNaoEncontradaException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.OffsetDateTime;
import java.util.Map;

@Provider
public class SimulacaoNaoEncontradaExceptionMapper implements ExceptionMapper<SimulacaoNaoEncontradaException> {

    @Override
    public Response toResponse(SimulacaoNaoEncontradaException exception) {
        Map<String, Object> payload = Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", 404,
                "erro", "Nao Encontrado",
                "mensagem", exception.getMessage()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(payload)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
