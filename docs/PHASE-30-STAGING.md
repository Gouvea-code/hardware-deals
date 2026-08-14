# FASE 30 — Staging

O ambiente está definido em `docker-compose.staging.yml` e usa volumes, banco, Redis, credenciais, portas e nome de projeto próprios. Ele nunca deve receber URL ou credencial de produção.

## Inicialização

1. Copie `.env.staging.example` para `.env.staging` fora do controle de versão.
2. Gere senha PostgreSQL e segredo JWT exclusivos.
3. Execute `docker compose --env-file .env.staging -f docker-compose.staging.yml up -d --build`.
4. Verifique `http://localhost:18080/actuator/health/readiness` e Mailpit em `http://localhost:18025`.
5. Execute os fluxos funcionais e depois o perfil k6 de 100 usuários.

Firebase e Alert Engine começam desabilitados. Ative-os apenas com credenciais próprias de staging e dispositivo de teste. A criação do servidor externo depende da escolha do provedor.
