# FASE 08 — Primeiro Collector

## Objetivo

Implementar o primeiro pipeline de coleta usando uma única fonte HTTP JSON configurável, sem assumir ou inventar uma API de loja.

## Pipeline

```text
JSON feed → validação → identificação básica → persistência de oferta e histórico
```

Componentes:

- `PriceCollector` — contrato de fontes de preço.
- `JsonFeedPriceCollector` — cliente da única fonte configurada.
- `CollectedOfferValidator` — valida campos, preços e URL.
- `CollectedOfferProcessor` — identifica loja e produto e persiste cada item em transação própria.
- `CollectorPipeline` — isola erros por item e registra o resultado.
- `CollectorJob` — agenda execuções quando habilitado.

## Configuração

O collector fica desabilitado por padrão. Variáveis:

- `COLLECTOR_ENABLED=true`
- `COLLECTOR_FEED_URL=https://fonte-autorizada.example/offers.json`
- `COLLECTOR_CRON=0 0 * * * *` — padrão: uma vez por hora.

A URL deve usar HTTP ou HTTPS. A loja indicada por `storeSlug` precisa existir e estar ativa.

## Contrato do feed

O endpoint deve retornar um array JSON:

```json
[
  {
    "storeSlug": "loja-autorizada",
    "externalId": "abc-123",
    "sku": "SKU-123",
    "productName": "Nome do produto",
    "brand": "Marca",
    "model": "Modelo",
    "category": "GPU",
    "ean": "7890000000000",
    "url": "https://loja.example/produto",
    "price": 1999.90,
    "originalPrice": 2199.90,
    "coupon": "CUPOM",
    "available": true,
    "collectedAt": "2026-08-13T18:00:00"
  }
]
```

`originalPrice`, `coupon`, `available` e `collectedAt` são opcionais. EAN identifica o produto nesta fase. A normalização especializada permanece reservada para a FASE 09.

## Tolerância a falhas

- Cada oferta é processada em transação independente.
- Item inválido é registrado e não interrompe os seguintes.
- Falha da fonte é registrada e não derruba o processo agendado.
- O resultado informa itens recebidos, persistidos e com falha.

## Testes

- Leitura e desserialização do feed HTTP configurado.
- Persistência de produto, vínculo da loja, oferta e histórico.
- Isolamento de item inválido entre itens válidos.
- Tratamento de indisponibilidade da fonte.

Resultado da suíte completa:

```text
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 08 concluída. Nenhum requisito de normalização avançada da FASE 09 foi iniciado.
