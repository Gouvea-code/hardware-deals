# FASE 31 — Produção

## Arquitetura preparada

- Nginx termina TLS, redireciona HTTP e encaminha tráfego à API.
- API Spring Boot executa migrations e expõe health checks.
- PostgreSQL e Redis não publicam portas externas.
- Prometheus coleta métricas internamente e publica sua interface somente em localhost.
- Serviço de manutenção produz backups PostgreSQL em formato customizado.

## Backup

- Frequência recomendada: diário, com backup adicional antes de migrations.
- Retenção inicial: 30 dias, ajustável por `BACKUP_RETENTION_DAYS`.
- Cópia: volume local mais armazenamento externo criptografado e versionado.
- Restore: mensal em banco isolado, com tempo e resultado registrados.

Backup manual: `docker compose --env-file .env.production -f docker-compose.production.yml --profile maintenance run --rm backup`.

Produção ainda requer provedor, cofre de segredos, certificado, SMTP, Firebase, armazenamento externo de backup, alertas e aprovação humana. Os arquivos do repositório não significam que um ambiente real já esteja publicado.
