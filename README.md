# Simulador de Financiamento API (Quarkus)

API backend para simular juros compostos de financiamentos/investimentos, gerar memoria de calculo mensal e persistir historico em H2 embutido (ficheiro), sem Docker.

## Autor

- Ramon Marques Coutinho - Matrícula: C078969
- Finalidade: Produção Temática – Av Técnica interna CAIXA
- PSI: 15262 - Talentos Digitais _ Caixaverso_DESENVOLVEDOR BACKEND JAVA _ASSISTENTE SENIOR (2058)
- PSI: 15263 - Talentos Digitais _ Caixaverso_DESENVOLVEDOR BACKEND JAVA _ASSISTENTE PLENO (2057)

## Stack

- Java 25
- Quarkus
- H2 Database (modo ficheiro)
- JPA/Hibernate Panache
- OpenAPI/Swagger UI automatico
- JUnit 5 + Rest Assured + JaCoCo

## Funcionalidades

- POST /simulacoes
  - Recebe valorInicial, taxaJurosMensal (%) e prazoMeses.
  - Calcula juros compostos com BigDecimal.
  - Persiste simulacao e memoria de calculo (mes a mes).
  - Retorna 201 com:
    - id
    - valorTotalFinal
    - valorTotalJuros
    - memoriaCalculo completa

- GET /simulacoes/{id}
  - Busca simulacao existente por ID.
  - Retorna objeto completo (entradas, totais e memoria).
  - Retorna 404 quando nao encontrado.

## Endpoints de Documentacao

- OpenAPI JSON: /openapi
- Swagger UI: /swagger

## Como executar

1. Confirmar Java 25 e Maven disponiveis:

```bash
java -version
mvn -version
```

2. Subir a aplicacao em modo dev:

```bash
mvn quarkus:dev
```

3. Acessar Swagger:

- http://localhost:8080/swagger

## Testes e Cobertura (criterio eliminatorio >= 80%)

Comando exato para rodar toda a suite e validar cobertura Jacoco:

```bash
mvn clean verify
```

O build falha automaticamente se a cobertura de linhas ficar abaixo de 80%.

## Persistencia H2

- Ambiente normal: base em ficheiro local em ./data/simulacoes
- Ambiente de teste: base em memoria (isolada)

## Estrutura de camadas

- Resource: endpoints HTTP
- Service: regras de negocio e calculo
- Repository: acesso a dados
- Entity/DTO: persistencia e contrato da API
