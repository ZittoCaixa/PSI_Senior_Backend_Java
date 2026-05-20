package br.com.psi.simulacao.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class SimulacaoResourceTest {

    @Test
    void deveCriarSimulacaoComSucesso() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 3
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("valorInicial", equalTo(1000.00F))
                .body("taxaJurosMensal", equalTo(1.5F))
                .body("prazoMeses", equalTo(3))
                .body("valorTotalFinal", equalTo(1045.67F))
                .body("valorTotalJuros", equalTo(45.67F))
                .body("memoriaCalculo", hasSize(3))
                .body("memoriaCalculo[0].mes", equalTo(1));
    }

    @Test
    void deveConsultarSimulacaoExistentePorId() {
        String payload = """
                {
                  "valorInicial": 1500.00,
                  "taxaJurosMensal": 1.0,
                  "prazoMeses": 2
                }
                """;

        Response criacao = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long id = criacao.jsonPath().getLong("id");

        given()
                .when()
                .get("/simulacoes/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("valorInicial", equalTo(1500.00F))
                .body("memoriaCalculo", hasSize(2));
    }

    @Test
    void deveRetornar404AoConsultarIdInexistente() {
        given()
                .when()
                .get("/simulacoes/{id}", 999999)
                .then()
                .statusCode(404)
                .body("mensagem", notNullValue());
    }

    @Test
    void deveRetornar400QuandoPayloadForInvalido() {
        String payload = """
                {
                  "valorInicial": 0,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 0
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400);
    }

    @Test
    void deveGerarEvolucaoMonotonaDosSaldos() {
        String payload = """
                {
                  "valorInicial": 300.00,
                  "taxaJurosMensal": 2.0,
                  "prazoMeses": 4
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .body("memoriaCalculo[0].saldoFinal", greaterThan(300.00F))
                .body("memoriaCalculo[1].saldoFinal", greaterThan(306.00F))
                .body("memoriaCalculo[3].mes", equalTo(4));
    }
}
