# FASE 03 — PostgreSQL com Testcontainers

## Objetivo
Conectar banco de dados PostgreSQL e validar migrations com Flyway.

## Abordagem Implementada

Devido a restrições do ambiente (Docker Desktop não inicializa), foi utilizada uma **abordagem moderna e escalável** com **Testcontainers**:

- **Testes unitários**: Usam H2 em memória (rápidos, sem dependências externas)
- **Testes de integração**: Usam Testcontainers com PostgreSQL real (apenas quando Docker disponível)
- **docker-compose.yml**: Preparado para CI/CD e ambiente de desenvolvimento (quando Docker funciona)

### Vantagens desta abordagem:
1. Testes unitários rodam localmente sem dependências externas
2. CI/CD pode rodar testes de integração com Docker
3. Flexibilidade total: escolher entre velocidade (H2) ou fidelidade (PostgreSQL real)

## Arquivos criados / modificados

### Configuration
- `docker-compose.yml` — Compose file com PostgreSQL 16 e Redis 7 (pronto para CI/CD)
- `pom.xml` — Adicionado Testcontainers JUnit e configurado maven-surefire-plugin

### Integration Tests
- `src/test/java/com/hardwaredeals/integration/PostgreSQLIntegrationTest.java` — Testa container PostgreSQL
- `src/test/java/com/hardwaredeals/integration/FlywayMigrationTest.java` — Testa Flyway migrations

### Profiles
- `application-test.yml` — Usa H2 em memória, Flyway desabilitado
- Integration tests têm anotação `@Testcontainers` e configuram PostgreSQL via `@DynamicPropertySource`

## Testes

### Unitários (H2 em memória) — Sempre rodam
```bash
mvn clean test
# Output:
# Tests run: 2, Failures: 0, Errors: 0
# BUILD SUCCESS
```

### Integração (PostgreSQL real) — Requerem Docker
```bash
mvn clean test -P integration
```
Só executa com Docker funcionando.

## Status de Validação

### ✅ Completo
- Testcontainers configurado (junit-jupiter, postgresql)
- Testes unitários validam contexto Spring Boot e health endpoint
- docker-compose.yml pronto com PostgreSQL e Redis
- Perfil Maven "integration" para testes com Docker

### ⚠️ Pendente
- Executar testes de integração com Docker ativo
- Validar Flyway migrations com schema real

## Próximos passos

- FASE 04: Modelo de domínio (User, Store, Product, etc)
- FASE 05: Autenticação e autorização

## Como usar docker-compose.yml

```bash
# Quando Docker estiver funcional:
docker-compose up -d           # Inicia PostgreSQL e Redis
docker-compose down            # Para containers
docker-compose logs postgres   # Ver logs
```

## Commit
```
feat: add PostgreSQL integration tests with Testcontainers (FASE 03)
```
