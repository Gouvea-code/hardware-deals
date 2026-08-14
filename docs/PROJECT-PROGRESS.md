# Progresso geral do Hardware Deals

Atualizado em 14/08/2026. O plano principal possui **51 fases numeradas de 00 a 50**.

## Percentual

- Concluído no repositório: **41 de 51 fases = 80,4%** (FASES 00 a 35 e 37 a 41).
- Restante ou adiado: **10 de 51 fases = 19,6%** (FASE 36 e FASES 42 a 50).

A FASE 36 foi adiada porque a distribuição atual é por APK, sem Play Store. As FASES 42 a 44 têm automação, roteiro e documentação prontos, mas dependem de API pública, aparelho físico e participantes reais; por isso ainda não são contadas como concluídas.

## Estado das próximas fases

| Fase | Estado | Entrega restante |
|---|---|---|
| 36 | Adiada | Play Console, somente se a estratégia mudar. |
| 41 | Concluída | Permissões mínimas auditadas e testadas. |
| 42 | Preparada | Gerar e validar seis screenshots reais com API pública. |
| 43 | Preparada | Executar roteiro interno em aparelho físico e corrigir bloqueios. |
| 44 | Preparada | Executar beta fechado direto e documentar resultados. |
| 45 | Adaptar | Fazer rollout gradual do APK fora da Play Store. |
| 46 | Pendente | Operar monitoramento, incidentes, feedback e atualizações. |
| 47 | Parcial | Conectores implementados; obter credenciais/contratos e ativar preços reais. |
| 48 | Pendente | Calibrar promoções com dados reais. |
| 49 | Pendente | Implementar denúncias enviadas por usuários. |
| 50 | Pendente | Avaliar monetização após métricas e conformidade. |

## Pendência imediata

Provisionar uma API HTTPS pública com PostgreSQL e Redis, configurar SMTP/Firebase e apontar `public-site/config.js` e o APK para essa URL. O código de produção já existe, mas criar o serviço requer uma conta de hospedagem, domínio/URL e segredos que não devem ser inventados nem enviados ao Git.

Em paralelo, o proprietário precisa obter acesso autorizado de Mercado Livre, Amazon Brasil, KaBuM! e Magazine Luiza. Sem contratos/credenciais válidos, nenhuma implementação pode legalmente produzir a lista real de preços; scraping permanece proibido.
