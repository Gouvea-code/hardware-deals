# FASE 26 — Testes negativos

## Objetivo

Confirmar que a API rejeita entradas inválidas, tokens sem validade e acessos indevidos sem expor dados de outros usuários.

## Cobertura implementada

O arquivo `NegativeApiTest` executa requisições HTTP reais contra o contexto Spring Boot e cobre:

| Cenário | Resultado esperado |
| --- | --- |
| E-mail com formato inválido | HTTP 400 |
| Senha incorreta no login | HTTP 401 |
| Access token expirado | HTTP 403, sem autenticar a requisição |
| Produto inexistente | HTTP 404 |
| Produto inexistente ao criar alerta | HTTP 404 |
| Preço zero, negativo ou ausente | HTTP 400 |
| Usuário anônimo em recurso protegido | HTTP 403 |
| Tentativa de remover alerta de outro usuário | HTTP 204 sem alterar o alerta do proprietário |

O retorno 204 na última situação é intencional: a remoção é idempotente e não revela se outro usuário possui aquele recurso.

## Validação

Comando executado:

```bash
cd backend
mvn --batch-mode test
```

Resultado local: **67 testes executados, zero falhas e zero erros**.

Os testes de integração com PostgreSQL e Redis permanecem separados no perfil Maven `integration` e são executados pela CI com Docker.

## Arquivos relevantes

- `backend/src/test/java/com/hardwaredeals/api/NegativeApiTest.java`
- `backend/src/test/java/com/hardwaredeals/controller/AuthControllerTest.java`
- `backend/src/test/java/com/hardwaredeals/controller/PriceAlertControllerTest.java`
- `.github/workflows/ci.yml`

## Próxima fase

A FASE 27 deve executar a revisão de segurança: HTTPS, JWT, rate limiting distribuído, CORS, validação de entrada, autorização, proteção contra injeção/XSS, gestão de segredos, logs e dependências.
