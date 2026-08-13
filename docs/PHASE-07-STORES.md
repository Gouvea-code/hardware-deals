# FASE 07 — Lojas

## Objetivo

Disponibilizar consultas públicas e somente de leitura para as lojas ativas cadastradas.

## Endpoints

- `GET /api/v1/stores` — lista lojas ativas ordenadas pelo nome.
- `GET /api/v1/stores/{id}` — consulta uma loja ativa pelo UUID.

## Comportamento

- As respostas usam DTO e não expõem entidades JPA.
- Lojas inativas não aparecem na listagem e retornam `404` na consulta individual.
- Loja inexistente retorna `404`.
- UUID inválido retorna `400`.
- Cadastro e desativação administrativa permanecem para uma fase futura, conforme o blueprint.

## Testes

`StoreControllerTest` cobre ordenação, exposição apenas de lojas ativas, consulta individual, loja inativa, loja inexistente e UUID inválido.

Suíte completa executada:

```text
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 07 concluída. Nenhum requisito da FASE 08 foi iniciado.
