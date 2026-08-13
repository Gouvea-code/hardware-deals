# FASE 14 — Tela Home

## Objetivo

Apresentar as ofertas mais relevantes, as categorias disponíveis e as ofertas
coletadas recentemente usando dados reais da API.

## Conteúdo

- melhores ofertas ordenadas pelo Deal Score.
- categorias derivadas do catálogo ativo de produtos.
- ofertas recentes ordenadas pela data de coleta.
- cards com imagem, nome, preço, preço anterior, score e loja.

## Estados da interface

- carregamento inicial.
- erro com nova tentativa.
- conteúdo vazio por seção.
- atualização por gesto de arrastar.

## Integração

A Home consulta `GET /api/v1/deals?sort=score`,
`GET /api/v1/deals?sort=recent` e `GET /api/v1/products?size=100`. O TanStack
Query gerencia cache, atualização e estado das requisições.

## Limites da fase

Categorias e cards são somente informativos. Busca, filtragem, navegação para
detalhes e abertura da loja serão implementadas nas fases correspondentes.

## Validação

- TypeScript sem erros.
- lint sem avisos.
- 6 testes mobile aprovados em 5 suítes.
- 41 testes do backend aprovados.

## Checkpoint

FASE 14 concluída. Nenhum requisito da FASE 15 foi iniciado.
