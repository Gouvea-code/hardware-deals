# FASE 10 — Histórico de Preços

## Objetivo

Expor a série histórica criada pelo collector e calcular estatísticas determinísticas de preço para um produto ativo.

## Endpoint

```http
GET /api/v1/products/{id}/price-history
```

O endpoint é público, seguindo as demais consultas de catálogo.

## Resposta

- `currentPrice` — último preço pela data de coleta.
- `lowestPrice` — menor preço registrado.
- `highestPrice` — maior preço registrado.
- `averagePrice` — média aritmética.
- `medianPrice` — valor central; para quantidade par, média dos dois valores centrais.
- `priceVariation` — `(currentPrice - averagePrice) / averagePrice * 100`.
- `history` — pontos ordenados do mais antigo para o mais recente, com loja, preço e data.

Valores monetários e percentuais usam duas casas decimais e arredondamento `HALF_UP`. Uma variação negativa indica que o preço atual está abaixo da média.

Produto ativo sem histórico retorna `200` com estatísticas nulas e lista vazia. Produto inexistente ou inativo retorna `404`.

## Persistência

O pipeline da FASE 08 já registra uma linha em `price_history` para toda coleta válida. A FASE 10 usa esses registros sem duplicar a persistência.

## Testes

- Série cronológica.
- Preço atual, mínimo, máximo e média.
- Mediana com quantidade ímpar e par.
- Variação percentual negativa.
- Produto sem histórico.
- Produto inexistente ou inativo.

Resultado da suíte completa:

```text
Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 10 concluída. Nenhum requisito da FASE 11 foi iniciado.
