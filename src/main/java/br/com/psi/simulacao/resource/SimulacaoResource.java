package br.com.psi.simulacao.resource;

import br.com.psi.simulacao.dto.SimulacaoRequest;
import br.com.psi.simulacao.dto.SimulacaoResponse;
import br.com.psi.simulacao.service.SimulacaoService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/simulacoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    private final SimulacaoService service;

    public SimulacaoResource(SimulacaoService service) {
        this.service = service;
    }

    @POST
    @Operation(summary = "Cria uma simulacao de juros compostos e persiste o resultado")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Simulacao criada com sucesso",
                    content = @Content(schema = @Schema(implementation = SimulacaoResponse.class))),
            @APIResponse(responseCode = "400", description = "Payload invalido")
    })
    public Response criar(@Valid SimulacaoRequest request) {
        SimulacaoResponse response = service.criar(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca uma simulacao existente por ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Simulacao encontrada",
                    content = @Content(schema = @Schema(type = SchemaType.OBJECT, implementation = SimulacaoResponse.class))),
            @APIResponse(responseCode = "404", description = "Simulacao nao encontrada")
    })
    public SimulacaoResponse buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }
}
