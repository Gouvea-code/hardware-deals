# FASE 28 — Performance

## Entregas

- Métricas HTTP automáticas em `http.server.requests`.
- Métricas do pool/banco via HikariCP e Actuator.
- `hardware_deals.collector.duration` e contadores de ofertas por origem/resultado.
- `hardware_deals.push.duration` separado por sucesso, token inválido e falha.
- Exportação Prometheus em `/actuator/prometheus`.
- Carga k6 parametrizada para 100, 1.000 e 10.000 usuários virtuais.

## Execução

```bash
docker compose -f performance/docker-compose.yml run --rm -e LOAD_PROFILE=100 k6
```

Os perfis maiores só devem ser executados em staging com autorização e capacidade monitorada. Limites iniciais: menos de 1% de erros, p95 abaixo de 500 ms e p99 abaixo de 1 segundo. Esses valores são hipóteses iniciais, não resultados: medições reais exigem o ambiente publicado e dados representativos.
