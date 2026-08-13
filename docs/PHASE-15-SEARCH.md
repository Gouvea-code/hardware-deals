# FASE 15 — Busca

## Objetivo

Permitir a localização de produtos por nome, marca ou modelo usando a API de
catálogo.

## Fluxo

- a Home oferece um acesso direto à busca.
- a consulta começa após dois caracteres.
- o termo é enviado 400 ms após a última alteração.
- cada consulta retorna até 20 produtos ativos.

## Estados da interface

- orientação antes da primeira consulta.
- carregamento durante a busca.
- lista de resultados com imagem, nome, marca e categoria.
- resultado vazio com sugestão para alterar o termo.
- erro com ação de nova tentativa.

## Endpoint

```text
GET /api/v1/products/search?q={termo}&page=0&size=20
```

## Limites da fase

Filtros avançados, paginação infinita e navegação para detalhes não fazem parte
desta fase.

## Validação

- TypeScript sem erros.
- lint sem avisos.
- serviços, cards e comportamento de debounce cobertos por testes.
- 9 testes mobile e 41 testes backend aprovados.

## Checkpoint

FASE 15 concluída. Nenhum requisito da FASE 16 foi iniciado.
