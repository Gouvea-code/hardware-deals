# FASE 09 — Normalização

## Objetivo

Normalizar identidades de produtos de forma determinística e conservadora para reconhecer apresentações equivalentes sem misturar variantes diferentes.

## Campos normalizados

- Fabricante/marca.
- Modelo.
- Nome do produto.
- SKU.
- EAN.
- Capacidades em KB, MB, GB e TB.
- Memória para categorias e descrições compatíveis.
- Variantes `XT`, `SUPER` e `Ti`.

## Regras

- Remove espaços excedentes, diferenças de caixa, acentos e pontuação.
- Separa limites entre letras e números: `RX9070XT` vira `rx 9070 xt`.
- Remove do nome canônico a marca informada e termos genéricos de família como Radeon e GeForce.
- Mantém capacidades e variantes no nome canônico.
- SKU é comparado sem separadores e em caixa alta.
- EAN mantém somente dígitos.

## Matching

Prioridade:

1. EAN normalizado.
2. Correspondência exata de marca e nome normalizado.

Não existe similaridade aproximada nesta fase. Essa decisão evita que `RX 9070`, `RX 9070 XT`, `RTX 4070 Ti` e versões `SUPER` sejam combinadas indevidamente.

O collector passou a usar `ProductNormalizer` e `ProductMatchingService`. Produtos equivalentes da mesma loja reutilizam o produto e o vínculo existentes, mas cada coleta ainda gera oferta e histórico.

## Banco

Foi adicionado índice composto em `products(brand, normalized_name)`. EAN continua único e é o identificador preferencial.

## Testes

Cobertura obrigatória:

- Espaços e caixa.
- Acentos e caracteres especiais.
- Formas longas e compactas.
- `XT`, `SUPER` e `Ti`.
- Capacidades e memória.
- SKU e EAN.
- Matching de nomes equivalentes.
- Não associação de variantes distintas.

Resultado da suíte completa:

```text
Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 09 concluída. Nenhum requisito da FASE 10 foi iniciado.
