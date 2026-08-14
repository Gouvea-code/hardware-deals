# Conectores de marketplaces

## O que foi implementado

O backend possui conectores independentes para Mercado Livre, Amazon Brasil, KaBuM! e Magazine Luiza. Todos recebem ofertas de uma API/feed autorizado no formato normalizado abaixo, passam pelo pipeline já existente e alimentam os mesmos endpoints consumidos pelo site e pelo APK.

Nenhum conector faz scraping. Cada canal fica desabilitado até que o proprietário do projeto obtenha contrato, credencial e endpoint permitidos pelo marketplace ou parceiro afiliado.

## Formato normalizado

O endpoint configurado deve responder um array JSON:

```json
[
  {
    "externalId": "identificador-na-loja",
    "sku": "sku",
    "productName": "Produto",
    "brand": "Marca",
    "model": "Modelo",
    "category": "GPU",
    "ean": "7890000000000",
    "url": "https://loja.example/produto",
    "price": 1999.90,
    "originalPrice": 2199.90,
    "coupon": null,
    "available": true,
    "collectedAt": "2026-08-14T12:00:00"
  }
]
```

O backend ignora qualquer `storeSlug` recebido e fixa a loja correspondente ao conector, evitando que um feed atribua uma oferta à loja errada.

## Variáveis

Para executar a coleta, defina `COLLECTOR_ENABLED=true`. Para cada loja, defina:

| Loja | Ativação | Endpoint | Token Bearer opcional |
|---|---|---|---|
| Mercado Livre | `MERCADO_LIVRE_ENABLED` | `MERCADO_LIVRE_API_URL` | `MERCADO_LIVRE_ACCESS_TOKEN` |
| Amazon Brasil | `AMAZON_BRASIL_ENABLED` | `AMAZON_BRASIL_API_URL` | `AMAZON_BRASIL_ACCESS_TOKEN` |
| KaBuM! | `KABUM_ENABLED` | `KABUM_API_URL` | `KABUM_ACCESS_TOKEN` |
| Magazine Luiza | `MAGAZINE_LUIZA_ENABLED` | `MAGAZINE_LUIZA_API_URL` | `MAGAZINE_LUIZA_ACCESS_TOKEN` |

Tokens são enviados apenas no cabeçalho `Authorization: Bearer`, nunca em URL ou log. Não coloque credenciais no Git.

## Limite atual e ativação real

- Mercado Livre: cadastrar uma aplicação e obter autorização para os recursos compatíveis com o modelo de negócio.
- Amazon Brasil: obter acesso ao programa/API vigente e colocar um adaptador autorizado na frente do formato normalizado.
- KaBuM!: contratar feed/API ou programa de afiliados que permita catálogo e preço.
- Magazine Luiza: obter credenciais das APIs/parceria comercial e adaptar a resposta ao contrato normalizado.

As quatro integrações estão implementadas do ponto de vista de transporte, autenticação, isolamento por loja e ingestão. Elas não retornam preços reais até a entrega desses acessos externos. Alterações nos contratos nativos ficam concentradas no adaptador, sem alterar o site ou o aplicativo.

## Site e APK

O APK já consulta `/api/v1/deals` e `/api/v1/products`. O site agora consulta `/api/v1/deals?sort=score`; configure `window.HARDWARE_DEALS_API_URL` em `public-site/config.js` durante o deploy. A API deve permitir a origem pública em `ALLOWED_ORIGINS`.

Com a API pública e ao menos um conector habilitado, site e APK passam a enxergar a mesma lista após a execução agendada do coletor.
