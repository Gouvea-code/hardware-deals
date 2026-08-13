# FASE 11 — Deal Engine

## Objetivo

Avaliar ofertas com fórmula simples, determinística e explicável, sem uso de IA.

## Entrada e saída

`DealEvaluationService` recebe preço atual, média, mínimo, máximo, disponibilidade e cupom. Retorna uma nota inteira de 0 a 100 e uma classificação.

## Fórmula

```text
score = 50 + averageComponent + historicalComponent + couponComponent
```

- Base neutra: 50.
- `averageComponent`: percentual abaixo da média multiplicado por 2, limitado entre -50 e +35.
- `historicalComponent`: 10 pontos quando o preço está no mínimo histórico; nos demais casos, posição linear entre máximo (0) e mínimo (10).
- `couponComponent`: 5 pontos quando existe cupom não vazio.
- Oferta indisponível: nota 0.
- Resultado final: arredondado e limitado entre 0 e 100.

O desconto frente à média é o fator principal. O cupom é apenas bônus e não consegue tornar excelente uma oferta neutra por si só.

## Classificação

- 90–100: `EXCELENTE`
- 80–89: `OTIMA`
- 70–79: `BOA`
- 60–69: `INTERESSANTE`
- 0–59: `NORMAL`

## Validação

Todos os preços devem ser positivos e o mínimo não pode superar o máximo. Entradas inválidas são rejeitadas.

## Testes

- Oferta excelente no mínimo histórico.
- Preço neutro.
- Bônus exato de cupom.
- Oferta indisponível.
- Limites 0 e 100.
- Todas as fronteiras de classificação.
- Entradas inválidas.

Resultado da suíte completa:

```text
Tests run: 36, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 11 concluída. Nenhum endpoint da FASE 12 foi iniciado.
