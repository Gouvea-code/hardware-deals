# FASE 02 — Backend inicial concluída

## Objetivo
Criar Spring Boot funcionando com:
- Configuração de profiles (local, prod, test)
- Actuator habilitado
- OpenAPI configurado
- Tratamento global de erros
- Endpoint `/health`
- Testes unitários e de integração

## Arquivos criados

### Backend Structure
- `backend/pom.xml` — Maven configuration com dependências
- `backend/src/main/java/com/hardwaredeals/HardwareDealsApplication.java` — Main class
- `backend/src/main/java/com/hardwaredeals/controller/HealthController.java` — Health endpoint
- `backend/src/main/java/com/hardwaredeals/config/GlobalExceptionHandler.java` — Global exception handling
- `backend/src/main/java/com/hardwaredeals/config/SecurityConfig.java` — Security configuration

### Configuration Files
- `application.yml` — Base configuration
- `application-local.yml` — Local development profile
- `application-prod.yml` — Production profile
- `application-test.yml` — Test profile (uses H2 in-memory database)

### Database
- `db/migration/V1_0__Initial_schema.sql` — Flyway migration placeholder

### Tests
- `HardwareDealsApplicationTests.java` — Context load test
- `HealthControllerTest.java` — Health endpoint API test

## Stack confirmado

- Java 21 LTS
- Spring Boot 3.2.0
- Spring Web, Security, Data JPA
- Bean Validation, Flyway
- PostgreSQL driver (para production)
- Redis (para caching)
- OpenAPI/Springdoc 2.2.0
- H2 (para testes)
- JUnit 5, Mockito, RestAssured
- Testcontainers

## Status dos testes

```
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Testes implementados:
1. **HealthControllerTest**: Valida que GET /api/v1/health retorna {"status": "UP"} com 200 OK
2. **HardwareDealsApplicationTests**: Valida que o contexto Spring Boot carrega corretamente

## Security

- Endpoint `/api/v1/health` permitido publicamente
- Endpoints `/actuator/**` permitidos publicamente
- Swagger UI acessível sem autenticação
- Sessões stateless (CSRF desabilitado para simplificar)

## Próximos passos

- FASE 03: Configurar PostgreSQL com Docker
- FASE 04: Implementar modelo de domínio (User, Store, Product, etc)
- FASE 05: Autenticação e autorização

## Commit

```
feat: initialize Spring Boot backend (FASE 02)
```

Pushed to: https://github.com/Gouvea-code/hardware-deals/commit/0f6fa77...
