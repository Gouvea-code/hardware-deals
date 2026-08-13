# FASE 06 — API de Produtos

## Objetivo

Disponibilizar consulta pública de produtos ativos, busca textual, filtros combináveis, ordenação controlada e paginação.

## Endpoints

Prefixo: `/api/v1/products`.

- `GET /api/v1/products`
- `GET /api/v1/products/{id}`
- `GET /api/v1/products/search?q=`

## Filtros e paginação

Os endpoints de listagem e busca aceitam:

- `category` — correspondência exata sem diferenciar maiúsculas/minúsculas.
- `brand` — correspondência exata sem diferenciar maiúsculas/minúsculas.
- `minPrice` e `maxPrice` — consideram apenas ofertas disponíveis de lojas e vínculos ativos.
- `store` — slug exato da loja, sem diferenciar maiúsculas/minúsculas.
- `sort` — `name_asc`, `name_desc`, `brand_asc` ou `newest`.
- `page` — índice iniciado em zero.
- `size` — entre 1 e 100; padrão 20.

A busca `q` procura parcialmente em nome, marca e modelo. Filtros podem ser combinados.

## Respostas e segurança

- Entidades JPA não são expostas diretamente; a API usa DTOs estáveis.
- Listagens retornam conteúdo e metadados de paginação.
- Produtos inativos não aparecem e retornam `404` na consulta individual.
- Parâmetros, UUIDs, preços, paginação e ordenação inválidos retornam `400`.
- Os três endpoints são públicos e somente de leitura.

## Testes

`ProductControllerTest` cobre listagem, paginação, ordenação, produto individual, produto inexistente, busca textual, combinação de filtros, preço, loja e entradas inválidas.

Suíte completa executada:

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 06 concluída. Nenhum requisito da FASE 07 foi iniciado.
