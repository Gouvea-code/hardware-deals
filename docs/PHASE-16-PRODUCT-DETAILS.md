# FASE 16 — Detalhes do produto

## Objetivo

Reunir dados de catálogo, ofertas e histórico em uma visão única do produto.

## Conteúdo

- produto, marca, categoria e imagem.
- preço atual, menor preço histórico e preço médio.
- melhor Deal Score disponível.
- ofertas por loja, incluindo preço e cupom.
- cinco pontos mais recentes do histórico em formato textual.
- estados de carregamento, erro e ausência de ofertas ou histórico.

## Navegação

Cards da Home e resultados da busca abrem a rota tipada `ProductDetails`.

## Integração

- `GET /api/v1/products/{id}`.
- `GET /api/v1/products/{id}/offers?sort=score`.
- `GET /api/v1/products/{id}/price-history`.

## Favoritos e alertas

As ações são apresentadas desabilitadas e identificadas como futuras. Os endpoints
de sincronização ainda não existem e pertencem, respectivamente, às FASES 18 e
19. Nenhum estado local temporário foi criado para evitar dados inconsistentes.

## Limites da fase

O gráfico interativo do histórico pertence à FASE 17. A série atual é exibida em
lista cronológica resumida.

## Validação

- TypeScript sem erros.
- lint sem avisos.
- integração dos três recursos coberta por teste automatizado.
- 10 testes mobile e 41 testes backend aprovados.

## Checkpoint

FASE 16 concluída. Nenhum requisito da FASE 17 foi iniciado.
