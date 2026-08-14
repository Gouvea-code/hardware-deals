# FASE 18 — Favoritos

## Implementação

- `GET /api/v1/favorites` lista favoritos do usuário autenticado.
- `PUT /api/v1/favorites/{productId}` adiciona de forma idempotente.
- `DELETE /api/v1/favorites/{productId}` remove de forma idempotente.
- a tela de favoritos e o detalhe usam cache e invalidação do TanStack Query.
- todos os dados são isolados pelo UUID extraído do JWT.

Sem sessão, a interface informa a necessidade de autenticação e não simula dados
locais. A interface completa de login ainda é uma pendência do aplicativo mobile.

## Checkpoint

FASE 18 concluída. Nenhum requisito da FASE 19 foi iniciado.
