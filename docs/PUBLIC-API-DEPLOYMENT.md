# Publicação da API HTTPS

## Blueprint preparado

O arquivo `render.yaml` cria uma API Docker, PostgreSQL 16 e armazenamento compatível com Redis. O provedor gera o segredo JWT e solicita os demais segredos no painel; nenhum valor confidencial entra no Git.

## Como publicar

1. Crie ou conecte uma conta Render ao repositório `Gouvea-code/hardware-deals`.
2. Escolha **New > Blueprint** e selecione o `render.yaml` da branch `main`.
3. Revise os planos: a API e o PostgreSQL são recursos pagos no blueprint atual; confirme o custo antes de criar.
4. Preencha `APP_PUBLIC_URL` com a URL HTTPS atribuída à API.
5. Configure SMTP. Mantenha Firebase e Alert Engine desligados até fornecer a credencial Firebase.
6. Gere uma nova chave do aplicativo Mercado Livre, conclua o OAuth e informe apenas o `access_token` em `MERCADO_LIVRE_ACCESS_TOKEN` no painel.
7. Confirme `/actuator/health/readiness` e `/api/v1/deals`.
8. Substitua `window.HARDWARE_DEALS_API_URL` em `public-site/config.js` pela URL da API e gere um novo APK com a mesma URL.

## Segurança

- Não reutilize a chave do Mercado Livre exposta em conversa.
- Não coloque client secret, access token, refresh token, SMTP ou Firebase em arquivos versionados.
- Restrinja o banco e o Redis à rede privada.
- Depois do primeiro deploy, habilite backup, alertas e retenção antes de convidar testadores.

## Bloqueio externo atual

O blueprint não cria recursos até o proprietário conectar e autorizar uma conta Render. Essa autorização pode gerar cobrança e não pode ser presumida pelo agente.
