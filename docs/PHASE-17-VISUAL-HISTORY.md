# FASE 17 — Histórico visual

## Objetivo

Representar a evolução do preço do produto de forma visual e acessível.

## Implementação

- gráfico de linha com `react-native-svg` 15.15.5.
- períodos de 7 dias, 30 dias e série completa.
- mínimo, máximo e preço atual recalculados para o período selecionado.
- suporte a um único ponto e preços sem variação.
- mensagens para série vazia e período sem dados.
- carregamento e erro herdados da consulta de detalhes.

O período é calculado em relação ao ponto mais recente da série, permitindo
visualizar corretamente históricos de ambientes de teste e dados importados.

## Checkpoint

FASE 17 concluída. Nenhum requisito da FASE 18 foi iniciado.
