# FASE 12 — API de Ofertas

## Objetivo

Expor ofertas atuais com preço, desconto, loja e avaliação do Deal Engine.

## Endpoints

- `GET /api/v1/deals?sort=score`
- `GET /api/v1/deals/{id}`
- `GET /api/v1/products/{id}/offers?sort=score`

Os endpoints são públicos e somente de leitura.

## Regras de exposição

- A listagem global e a listagem por produto retornam somente a coleta mais recente de cada `StoreProduct`.
- Ofertas indisponíveis, produtos inativos, lojas inativas e vínculos inativos não são expostos.
- A consulta individual usa o UUID da oferta e retorna `404` quando ela não está visível.
- Produto inexistente ou inativo retorna `404` em `/products/{id}/offers`.

## Ordenação

- `score` — maior Deal Score primeiro; padrão.
- `price` — menor preço primeiro.
- `discount` — maior desconto frente ao preço original primeiro.
- `recent` — coleta mais recente primeiro.

Valor inválido retorna `400`.

## Resposta

Inclui IDs, produto, marca, imagem, loja, preços, percentual de desconto, cupom, disponibilidade, URL externa, data da coleta, score e classificação.

As estatísticas usadas pelo Deal Engine são calculadas a partir do histórico do produto. Quando ainda existe apenas um ponto, o próprio preço serve como média, mínimo e máximo.

## Testes

- Deduplicação de coletas antigas.
- Ordenações por score, preço, desconto e data.
- Oferta individual e ofertas por produto.
- Score e classificação na resposta.
- Ocultação de ofertas indisponíveis.
- Recursos inexistentes e ordenação inválida.

Resultado da suíte completa:

```text
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 12 concluída. Nenhum requisito da FASE 13 foi iniciado.
